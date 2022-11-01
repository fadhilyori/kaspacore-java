package org.mataelang.kaspacore.providers;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

public class Producer extends KafkaProvider {
    private static Producer instance;

    protected KafkaProducer<String, JsonNode> kafkaProducer;

    public Producer() {
        setConfig("bootstrap.servers", "KAFKA_BOOTSTRAP_SERVERS");
        setConfig("acks", "KAFKA_OUTPUT_ACKS");
        setConfig("retries", "KAFKA_OUTPUT_RETRIES");
        setConfig("key.serializer", "KAFKA_INPUT_KEY_SERIALIZER", "org.apache.kafka.common.serialization.StringSerializer");
        setConfig("value.serializer", "KAFKA_INPUT_VALUE_SERIALIZER", "io.confluent.kafka.serializers.KafkaJsonSerializer");
    }

    public static Producer getInstance() {
        if (instance == null) {
            synchronized (Producer.class) {
                instance = new Producer();
            }
        }

        return instance;
    }

    public void connect() {
        kafkaProducer = new KafkaProducer<>(getConfig());
        Logger.getLogger(Producer.class).debug("Kafka Producer connection established");
    }

    public void close() {
        kafkaProducer.close();
        Logger.getLogger(Producer.class).debug("Kafka Producer has been closed");
    }

    public void send(String topic, JsonNode message) {
        kafkaProducer.send(new ProducerRecord<>(topic, message));
    }
}
