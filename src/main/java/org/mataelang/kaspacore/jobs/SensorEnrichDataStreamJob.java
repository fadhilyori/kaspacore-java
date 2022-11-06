package org.mataelang.kaspacore.jobs;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Producer;
import org.mataelang.kaspacore.providers.Spark;
import org.mataelang.kaspacore.utils.IPLookupTool;
import org.mataelang.kaspacore.utils.PropertyManager;

public class SensorEnrichDataStreamJob {
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger(SensorEnrichDataStreamJob.class).setLevel(
                Level.toLevel(
                        PropertyManager.getProperty("LOG_LEVEL"),
                        Level.DEBUG
                )
        );

        Consumer.getInstance().getStream(Spark.getStreamingContext()).foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                Producer.getInstance().connect();

//                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];

                // send to kafka
                recordIterator.forEachRemaining(message -> {
                    ObjectNode objectNode = IPLookupTool.ipEnrichmentFunc(message);

                    // send to kafka
                    Producer.getInstance().send(PropertyManager.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"), objectNode);
                });

                Producer.getInstance().close();
            });

            ((CanCommitOffsets) Consumer.getInstance().getStream(Spark.getStreamingContext()).inputDStream()).commitAsync(offsetRanges);
        });

        Spark.getStreamingContext().start();
        Spark.getStreamingContext().awaitTermination();
    }
}