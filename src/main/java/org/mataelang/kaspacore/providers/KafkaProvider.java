package org.mataelang.kaspacore.providers;

import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.HashMap;
import java.util.Map;

public class KafkaProvider {
    protected Map<String, Object> config;

    public KafkaProvider() {
        config = new HashMap<>();
    }

    protected Map<String, Object> getConfig() {
        return config;
    }

    protected void setConfig(String key, String propertyName) {
        config.put(key, PropertyManager.getProperty(propertyName));
    }

    protected void setConfig(String key, String propertyName, String defaultValue) {
        config.put(key, PropertyManager.getProperty(propertyName, defaultValue));
    }

    protected void setConfigValue(String key, String value) {
        config.put(key, value);
    }
}
