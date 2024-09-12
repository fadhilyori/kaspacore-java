package org.mataelang.kaspacore.jobs;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Producer;
import org.mataelang.kaspacore.providers.Spark;
import org.mataelang.kaspacore.utils.IPLookupTool;
import org.mataelang.kaspacore.utils.PropertyManager;

public class SensorEnrichDataStreamJob {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger(SensorEnrichDataStreamJob.class).setLevel(
                Level.toLevel(
                        PropertyManager.getProperty("LOG_LEVEL"),
                        Level.DEBUG
                )
        );

        String appName = "SensorEnrichmentStream";

        JavaStreamingContext streamingContext = Spark.getStreamingContext(appName);

        Logger.getLogger(SensorEnrichDataStreamJob.class).info("Starting kaspacore enrichment service...");

        Consumer.getInstance().getStream(streamingContext).foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                Producer.getInstance().connect();

                // send to kafka
                recordIterator.forEachRemaining(message -> {
                    ObjectNode objectNode = IPLookupTool.ipEnrichmentFunc(message);

                    // send to kafka
                    Producer.getInstance().send(PropertyManager.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"), objectNode);
                });

                Producer.getInstance().close();
            });

            ((CanCommitOffsets) Consumer.getInstance().getStream(streamingContext).inputDStream()).commitAsync(offsetRanges);
            Logger.getLogger(SensorEnrichDataStreamJob.class).info("iteration finished");
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}
