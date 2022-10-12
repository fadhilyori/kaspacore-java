package org.mataelang.kaspacore.providers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.Collections;

public class Consumer extends KafkaProvider {
    public Consumer() {
        this.setConfig("bootstrap.servers", "inputBootstrapServers");
        this.setConfig("group.id", "groupID");
        this.setConfig("auto.offset.reset", "autoOffsetReset");
        this.setConfig("key.deserializer", "inputKeyDeserializer");
        this.setConfig("value.deserializer", "inputValueDeserializer");
        this.setConfig("enable.auto.commit", "inputEnableAutoCommit", false);
        this.setTopic(configLoader.getString("inputTopic"));
    }

    public JavaInputDStream<ConsumerRecord<String, String>> createStream(JavaStreamingContext javaStreamingContext) {
        return KafkaUtils
                .createDirectStream(
                        javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(Collections.singleton(this.getTopic()), this.getConfigs())
                );
    }
}
