package org.mataelang.kaspacore.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static PropertyManager instance;
    private Properties props;

    public PropertyManager() {
        loadProperties();
    }

    private void loadProperties() {
        this.props = new Properties();
        InputStream inputStream;
        ClassLoader classLoader = this.getClass().getClassLoader();

        String PROPERTIES_FILENAME = "app.properties";
        inputStream = classLoader.getResourceAsStream(PROPERTIES_FILENAME);

        if (inputStream == null) {
            throw new RuntimeException("Unable to open properties file "+ "app.properties");
        }

        try {
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            synchronized (PropertyManager.class) {
                instance = new PropertyManager();
            }
        }

        return instance;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
