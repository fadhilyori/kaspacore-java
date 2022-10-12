package org.mataelang.kaspacore;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.TaskContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Producer;
import org.mataelang.kaspacore.utils.ConfigLoader;

public class DataStream {
    public static void main(String[] args) throws InterruptedException {
        Logger log = Logger.getLogger(DataStream.class.getName());
        ConfigLoader config = new ConfigLoader();

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(config.getString("applicationName"));
        sparkConf.setMaster(config.getString("sparkMaster"));

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        Consumer consumer = new Consumer();

        JavaInputDStream<ConsumerRecord<String, String>> stream = consumer.createStream(streamingContext);

        stream.foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                Producer producer = new Producer();
                producer.connect();

                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];
                log.debug(o.topic() + " " + o.partition() + " " + o.fromOffset() + " " + o.untilOffset());

                // TODO: data processing

                // send to kafka
                recordIterator.forEachRemaining(message -> producer.send(message.value()));

                producer.close();
            });

            ((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges);
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}