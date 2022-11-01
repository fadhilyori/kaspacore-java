package org.mataelang.kaspacore.outputs;

import org.mataelang.kaspacore.utils.PropertyManager;

public class KafkaOutput extends StreamOutput {
    public KafkaOutput(String topic) {
        setOption("kafka.bootstrap.servers", PropertyManager.getProperty("KAFKA_BOOTSTRAP_SERVERS"));
        setFormat("kafka");
        setOption("topic", topic);
    }
}
