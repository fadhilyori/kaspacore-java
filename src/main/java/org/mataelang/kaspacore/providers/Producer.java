package org.mataelang.kaspacore.providers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer extends KafkaProvider {
    KafkaProducer<String, String> kafkaProducer;
    public Producer() {
        this.setConfig("bootstrap.servers", "outputBootstrapServers");
        this.setConfig("acks", "outputAcks");
        this.setConfig("retries", "outputRetries");
        this.setConfig("key.serializer", "outputKeySerializer");
        this.setConfig("value.serializer", "outputValueSerializer");
        this.topic = configLoader.getString("outputTopic");
    }

    public void connect() {
        this.kafkaProducer = new KafkaProducer<>(this.kafkaProducerParams);
    }

    public void close() {
        this.kafkaProducer.close();
    }

    public void send(String message) {
        this.kafkaProducer.send(new ProducerRecord<>(this.topic, message));
    }
}
