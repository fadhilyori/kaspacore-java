package org.mataelang.kaspacore.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    private static Producer instance;
    protected Map<String, Object> config;

    protected KafkaProducer<String, JsonNode> kafkaProducer;
    public Producer() {
        config = new HashMap<>();
        setConfig("bootstrap.servers", "KAFKA_BOOTSTRAP_SERVERS");
        setConfig("acks", "KAFKA_OUTPUT_ACKS");
        setConfig("retries", "KAFKA_OUTPUT_RETRIES");
        setConfig("key.serializer", "KAFKA_INPUT_KEY_SERIALIZER");
        setConfig("value.serializer", "KAFKA_INPUT_VALUE_SERIALIZER");
    }

    private void setConfig(String key, String propertyName) {
        config.put(key, PropertyManager.getProperty(propertyName));
    }

    public void connect() {
        kafkaProducer = new KafkaProducer<>(config);
    }

    public void close() {
        kafkaProducer.close();
    }

    public void send(String topic, JsonNode message) {
        kafkaProducer.send(new ProducerRecord<>(topic, message));
    }

    public static Producer getInstance() {
        if (instance == null) {
            synchronized (Producer.class) {
                instance = new Producer();
            }
        }

        return instance;
    }
}
