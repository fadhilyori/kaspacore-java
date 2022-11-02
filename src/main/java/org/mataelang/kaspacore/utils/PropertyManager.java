package org.mataelang.kaspacore.utils;

import org.apache.log4j.Logger;
import org.mataelang.kaspacore.exceptions.PropertyRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static PropertyManager instance;

    private Properties props;

    public PropertyManager() {
        try {
            loadProperties();
        } catch (IOException e) {
            throw new PropertyRuntimeException(e);
        }
    }

    private void loadProperties() throws IOException {
        Logger.getLogger(PropertyManager.class).debug("Loading properties file...");
        this.props = new Properties();
        InputStream inputStream;
        ClassLoader classLoader = this.getClass().getClassLoader();

        String propertiesFilename = "app.properties";
        inputStream = classLoader.getResourceAsStream(propertiesFilename);

        if (inputStream == null) {
            throw new FileNotFoundException("Unable to open properties file "+ "app.properties");
        }

        props.load(inputStream);
        Logger.getLogger(PropertyManager.class).info("Configuration loaded.");
        inputStream.close();
    }

    public Properties getProps() {
        return props;
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            synchronized (PropertyManager.class) {
                instance = new PropertyManager();
            }
        }

        return instance;
    }

    public static String getProperty(String key) {
        return getInstance().getProps().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getInstance().getProps().getProperty(key, defaultValue);
    }
}
