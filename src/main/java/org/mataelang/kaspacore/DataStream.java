package org.mataelang.kaspacore;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.kafka010.*;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Producer;
import org.mataelang.kaspacore.providers.Spark;

import java.util.Arrays;
import java.util.regex.Pattern;

public class DataStream {
    private static final Pattern SPACE = Pattern.compile(" ");
    public static void main(String[] args) throws InterruptedException {
        Logger log = Logger.getLogger(DataStream.class.getName());

//        JavaDStream<String> lines =
//                Consumer.getInstance().getStream(Spark.getStreamingContext()).map(ConsumerRecord::value);
//        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(Arrays.stream(SPACE.split(x)).iterator()));

        Consumer.getInstance().getStream(Spark.getStreamingContext()).foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                Producer.getInstance().connect();

                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];
//                log.debug(o.topic() + " " + o.partition() + " " + o.fromOffset() + " " + o.untilOffset());

                // send to kafka
                recordIterator.forEachRemaining(message -> {
                    // TODO: src_addr
                    // TODO: lookup ip addr with maxmind geoip
                    // TODO: add the value with src_country_code key into the record

                    // TODO: send to kafka

                    Producer.getInstance().send(message.value());
                });

                Producer.getInstance().close();
            });

            ((CanCommitOffsets) Consumer.getInstance().getStream(Spark.getStreamingContext()).inputDStream()).commitAsync(offsetRanges);
        });

        Spark.getStreamingContext().start();
        Spark.getStreamingContext().awaitTermination();
    }
}