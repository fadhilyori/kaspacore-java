package org.mataelang.kaspacore.providers;

import com.fasterxml.jackson.databind.JsonNode;
import io.confluent.kafka.schemaregistry.json.JsonSchema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    private static Producer instance;
    protected String topic;
    protected Map<String, Object> config;

    protected KafkaProducer<String, JsonNode> kafkaProducer;
    public Producer() {
        config = new HashMap<>();
        setConfig("bootstrap.servers", "outputBootstrapServers");
        setConfig("acks", "outputAcks");
        setConfig("retries", "outputRetries");
        setConfig("key.serializer", "outputKeySerializer");
        setConfig("value.serializer", "outputValueSerializer");
        topic = PropertyManager.getInstance().getProperty("outputTopic");
    }

    private void setConfig(String key, String propertyName) {
        config.put(key, PropertyManager.getInstance().getProperty(propertyName));
    }

    public void connect() {
        kafkaProducer = new KafkaProducer<>(config);
    }

    public void close() {
        kafkaProducer.close();
    }

    public void send(JsonNode message) {
        kafkaProducer.send(new ProducerRecord<>(topic, message));
    }
    public void sendThenClose(JsonNode message) {
        kafkaProducer.send(new ProducerRecord<>(topic, message));
        kafkaProducer.close();
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
