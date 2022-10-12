package org.mataelang.kaspacore;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.TaskContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import scala.Tuple2;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DataStreamPoC {
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(DataStream.class.getName());
    public static Properties loadConfig(String filename) throws IOException {
        try (InputStream inputStream = DataStream.class.getClassLoader().getResourceAsStream(filename)) {
            Properties properties = new Properties();

            if (inputStream == null) {
                log.error("Sorry, unable to find " + filename);
                throw new IOException("Sorry, unable to find " + filename);
            }

            properties.load(inputStream);
            return properties;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Properties properties;

        try {
            properties = loadConfig("app.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(properties.getProperty("applicationName"));
        sparkConf.setMaster(properties.getProperty("sparkMaster"));

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", properties.getProperty("inputBootstrapServers"));
        kafkaParams.put("key.deserializer", properties.getProperty("inputKeyDeserializer"));
        kafkaParams.put("value.deserializer", properties.getProperty("inputValueDeserializer"));
        kafkaParams.put("group.id", properties.getProperty("groupID"));
        kafkaParams.put("auto.offset.reset", properties.getProperty("autoOffsetReset"));
        kafkaParams.put("enable.auto.commit", false);

        Map<String, Object> kafkaProducerParams = new HashMap<>();
        kafkaProducerParams.put("bootstrap.servers", properties.getProperty("outputBootstrapServers"));
        kafkaProducerParams.put("acks", properties.getProperty("outputAcks"));
        kafkaProducerParams.put("retries", Integer.valueOf(properties.getProperty("outputRetries")));
        kafkaProducerParams.put("key.serializer", properties.getProperty("outputKeySerializer"));
        kafkaProducerParams.put("value.serializer", properties.getProperty("outputValueSerializer"));

        Collection<String> topics = List.of(properties.getProperty("kafkaTopics"));

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils
                .createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams)
                );

        stream.mapToPair(record -> new Tuple2<>(record.key(), record.value()));

        stream.foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(kafkaProducerParams);
                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];
                log.debug(o.topic() + " " + o.partition() + " " + o.fromOffset() + " " + o.untilOffset());
                recordIterator.forEachRemaining(stringStringConsumerRecord -> kafkaProducer.send(new ProducerRecord<>(properties.getProperty("outputTopic"), stringStringConsumerRecord.value())));
                kafkaProducer.close();
            });

            ((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges);
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}