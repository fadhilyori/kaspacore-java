package org.mataelang.kaspacore.providers;

import org.mataelang.kaspacore.utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class KafkaProvider {
    protected Map<String, Object> kafkaProducerParams;
    protected String topic;
    protected ConfigLoader configLoader;
    public KafkaProvider() {
        kafkaProducerParams = new HashMap<>();
        configLoader = new ConfigLoader();
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topicName) {
        this.topic = topicName;
    }

    public void setConfig(String key, String propertyName) {
        kafkaProducerParams.put(key, configLoader.get(propertyName));
    }

    public void setConfig(String key, String propertyName, Object defaultValue) {
        Object configValue = configLoader.get(propertyName);
        if (configValue == null) {
            configValue = defaultValue;
        }
        kafkaProducerParams.put(key, configValue);
    }

    public Map<String, Object> getConfigs() {
        return this.kafkaProducerParams;
    }
}
