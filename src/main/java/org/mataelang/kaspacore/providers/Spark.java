package org.mataelang.kaspacore.providers;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Spark {
    private static SparkConf sparkConf;
    private static JavaStreamingContext streamingContext;

    public static JavaStreamingContext getStreamingContext() {
        if (sparkConf == null) {
            sparkConf = new SparkConf();
            sparkConf.setAppName(PropertyManager.getInstance().getProperty("applicationName"));
            sparkConf.setMaster(PropertyManager.getInstance().getProperty("sparkMaster"));
        }

        if (streamingContext == null) {
            streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));
        }

        return streamingContext;
    }
}
