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
    static Logger log = Logger.getLogger(DataStreamPoC.class);
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
        sparkConf.setAppName(properties.getProperty("SPARK_APP_NAME"));
        sparkConf.setMaster(properties.getProperty("SPARK_MASTER"));

        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", properties.getProperty("KAFKA_BOOTSTRAP_SERVERS"));
        kafkaParams.put("key.deserializer", properties.getProperty("KAFKA_INPUT_KEY_DESERIALIZER"));
        kafkaParams.put("value.deserializer", properties.getProperty("KAFKA_INPUT_VALUE_DESERIALIZER"));
        kafkaParams.put("group.id", properties.getProperty("KAFKA_GROUP_ID"));
        kafkaParams.put("auto.offset.reset", properties.getProperty("KAFKA_INPUT_STARTING_OFFSETS"));
        kafkaParams.put("enable.auto.commit", false);

        Map<String, Object> kafkaProducerParams = new HashMap<>();
        kafkaProducerParams.put("bootstrap.servers", properties.getProperty("KAFKA_BOOTSTRAP_SERVERS"));
        kafkaProducerParams.put("acks", properties.getProperty("KAFKA_OUTPUT_ACKS"));
        kafkaProducerParams.put("retries", Integer.valueOf(properties.getProperty("KAFKA_OUTPUT_RETRIES")));
        kafkaProducerParams.put("key.serializer", properties.getProperty("KAFKA_INPUT_KEY_SERIALIZER"));
        kafkaProducerParams.put("value.serializer", properties.getProperty("KAFKA_INPUT_VALUE_SERIALIZER"));

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
                recordIterator.forEachRemaining(stringStringConsumerRecord -> kafkaProducer.send(new ProducerRecord<>(properties.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"), stringStringConsumerRecord.value())));
                kafkaProducer.close();
            });

            ((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges);
        });

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}