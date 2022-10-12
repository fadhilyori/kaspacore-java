package org.mataelang.kaspacore.utils;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.mataelang.kaspacore.DataStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    protected Properties properties;
    static Logger log = Logger.getLogger(DataStream.class.getName());

    public ConfigLoader() {
        try {
            this.properties = loadConfig("app.properties");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }
    public Object get(String key) {
        return this.properties.get(key);
    }
    @NotNull
    public static Properties loadConfig(String filename) throws IOException {
        try (InputStream inputStream = DataStream.class.getClassLoader().getResourceAsStream(filename)) {
            Properties properties = new Properties();

            if (inputStream == null) {
                throw new IOException("Sorry, unable to find " + filename);
            }

            properties.load(inputStream);
            return properties;
        }
    }
}
