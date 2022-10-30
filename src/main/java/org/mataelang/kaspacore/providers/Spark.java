package org.mataelang.kaspacore.providers;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.mataelang.kaspacore.schemas.EventSchema;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Spark {
    private static SparkConf sparkConf;
    private static JavaStreamingContext streamingContext;

    private Spark() {
    }

    public static JavaStreamingContext getStreamingContext() {
        if (sparkConf == null) {
            sparkConf = new SparkConf();
            sparkConf.setAppName(PropertyManager.getInstance().getProperty("applicationName"));
            sparkConf.setMaster(PropertyManager.getInstance().getProperty("sparkMaster"));
            sparkConf.validateSettings();
        }

        if (streamingContext == null) {
            streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));
        }

        return streamingContext;
    }

    public static SparkSession getSparkSession() {
        return SparkSession
                .builder()
                .appName(PropertyManager.getInstance().getProperty("applicationName"))
                .master(PropertyManager.getInstance().getProperty("sparkMaster"))
                .config("spark.sql.session.timeZone", "Asia/Jakarta")
                .getOrCreate();
    }

    public static Dataset<Row> getSparkKafkaStream() {
        return Spark.getSparkKafkaStream(Spark.getSparkSession());
    }

    public static Dataset<Row> getSparkKafkaStream(SparkSession sparkSession) {
        return sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getInstance().getProperty("inputBootstrapServers"))
                .option("startingOffsets", PropertyManager.getInstance().getProperty("autoOffsetReset"))
                .option("subscribe", PropertyManager.getInstance().getProperty("outputTopic"))
                .load();
    }

    public static Dataset<Row> getSparkKafkaStreamParsed() {
        return Spark.getSparkKafkaStream().select(
                functions.from_json(
                        functions.col("value").cast("string"),
                        EventSchema.getSchema()
                ).alias("parsed_value"),
                functions.col("timestamp")
        ).select(
                functions.col("parsed_value.*"),
                functions.col("timestamp").as("event_arrived_time")
        );
    }
}
