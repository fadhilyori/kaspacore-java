package org.mataelang.kaspacore.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Consumer {
    private static Consumer instance;
    protected Map<String, Object> config = new HashMap<>();
    private JavaInputDStream<ConsumerRecord<String, JsonNode>> stream;

    public Consumer() {
        setConfig("bootstrap.servers", "KAFKA_BOOTSTRAP_SERVERS");
        setConfig("group.id", "KAFKA_GROUP_ID");
        setConfig("auto.offset.reset", "KAFKA_INPUT_STARTING_OFFSETS");
        setConfig("key.deserializer", "KAFKA_INPUT_KEY_DESERIALIZER");
        setConfig("value.deserializer", "KAFKA_INPUT_VALUE_DESERIALIZER");
        setConfig("enable.auto.commit", "inputEnableAutoCommit", false);
        config.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, "com.fasterxml.jackson.databind.JsonNode");
    }

    public static Consumer getInstance() {
        if (instance == null) {
            instance = new Consumer();
        }
        return instance;
    }

    private void setConfig(String key, String propertyName) {
        config.put(key, PropertyManager.getProperty(propertyName));
    }

    @SuppressWarnings("SameParameterValue")
    private void setConfig(String key, String propertyName, Object defaultValue) {
        Object configValue = PropertyManager.getProperty(propertyName);
        if (configValue == null) {
            configValue = defaultValue;
        }
        config.put(key, configValue);
    }

    public JavaInputDStream<ConsumerRecord<String, JsonNode>> getStream(JavaStreamingContext javaStreamingContext) {
        String topic = PropertyManager.getProperty("SENSOR_STREAM_INPUT_TOPIC");

        if (stream == null) {
            stream = KafkaUtils
                    .createDirectStream(
                            javaStreamingContext,
                            LocationStrategies.PreferConsistent(),
                            ConsumerStrategies.Subscribe(Collections.singleton(topic), config)
                    );
        }

        return stream;
    }
}
