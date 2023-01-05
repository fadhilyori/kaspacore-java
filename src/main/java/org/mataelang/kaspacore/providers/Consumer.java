package org.mataelang.kaspacore.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.Collections;

public class Consumer extends KafkaProvider {
    private static Consumer instance;
    private JavaInputDStream<ConsumerRecord<String, JsonNode>> stream;

    public Consumer() {
        setConfig("bootstrap.servers", "KAFKA_BOOTSTRAP_SERVERS");
        setConfig("group.id", "KAFKA_GROUP_ID", "me_kaspacore");
        setConfig("auto.offset.reset", "KAFKA_INPUT_STARTING_OFFSETS", "earliest");
        setConfig("key.deserializer", "KAFKA_INPUT_KEY_DESERIALIZER", "org.apache.kafka.common.serialization.StringDeserializer");
        setConfig("value.deserializer", "KAFKA_INPUT_VALUE_DESERIALIZER", "io.confluent.kafka.serializers.KafkaJsonDeserializer");
        setConfig("enable.auto.commit", "inputEnableAutoCommit", "false");
        setConfigValue(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, "com.fasterxml.jackson.databind.JsonNode");
    }

    public static Consumer getInstance() {
        if (instance == null) {
            instance = new Consumer();
        }
        return instance;
    }

    public JavaInputDStream<ConsumerRecord<String, JsonNode>> getStream(JavaStreamingContext javaStreamingContext) {
        String topic = PropertyManager.getProperty("SENSOR_STREAM_INPUT_TOPIC");

        if (stream == null) {
            Logger.getLogger(PropertyManager.class).debug("Creating stream class..");
            stream = KafkaUtils
                    .createDirectStream(
                            javaStreamingContext,
                            LocationStrategies.PreferConsistent(),
                            ConsumerStrategies.Subscribe(Collections.singleton(topic), getConfig())
                    );
            Logger.getLogger(PropertyManager.class).debug("Class stream created.");
        }

        return stream;
    }
}
