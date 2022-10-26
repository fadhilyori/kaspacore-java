package org.mataelang.kaspacore.utils;

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
        this.props = new Properties();
        InputStream inputStream;
        ClassLoader classLoader = this.getClass().getClassLoader();

        String propertiesFilename = "app.properties";
        inputStream = classLoader.getResourceAsStream(propertiesFilename);

        if (inputStream == null) {
            throw new FileNotFoundException("Unable to open properties file "+ "app.properties");
        }

        props.load(inputStream);
        inputStream.close();
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
