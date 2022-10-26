package org.mataelang.kaspacore.providers;

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

    protected KafkaProducer<String, JsonSchema> producer;
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
        producer = new KafkaProducer<>(config);
    }

    public void close() {
        producer.close();
    }

    public void send(JsonSchema message) {
        producer.send(new ProducerRecord<>(topic, message));
    }
    public void sendThenClose(JsonSchema message) {
        producer.send(new ProducerRecord<>(topic, message));
        producer.close();
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
