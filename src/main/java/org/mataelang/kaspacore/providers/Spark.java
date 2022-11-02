package org.mataelang.kaspacore.providers;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.mataelang.kaspacore.utils.Functions;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Spark {
    private static JavaStreamingContext streamingContext;

    private Spark() {
    }

    private static SparkConf getSparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(PropertyManager.getProperty("SPARK_APP_NAME"));
        sparkConf.setMaster(PropertyManager.getProperty("SPARK_MASTER"));
        sparkConf.set("spark.sql.streaming.checkpointLocation", PropertyManager.getProperty("SPARK_CHECKPOINT_PATH"));
        sparkConf.set("spark.sql.session.timeZone", PropertyManager.getProperty("TIMEZONE", "Asia/Jakarta"));
        sparkConf.validateSettings();

        return sparkConf;
    }

    public static JavaStreamingContext getStreamingContext() {
        if (streamingContext == null) {
            streamingContext = new JavaStreamingContext(getSparkConf(), Durations.seconds(1));
        }

        return streamingContext;
    }

    public static SparkSession getSparkSession() {
        return SparkSession
                .builder()
                .config(getSparkConf())
                .getOrCreate();
    }

    public static Dataset<Row> getSparkKafkaStream() {
        return Spark.getSparkKafkaStream(Spark.getSparkSession());
    }

    public static Dataset<Row> getSparkKafkaStream(SparkSession sparkSession) {
        return sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getProperty("KAFKA_BOOTSTRAP_SERVERS"))
                .option("startingOffsets", PropertyManager.getProperty("KAFKA_INPUT_STARTING_OFFSETS", "latest"))
                .option("subscribe", PropertyManager.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"))
                .load();
    }

    public static Dataset<Row> getSparkKafkaStreamParsed() {
        return Spark.getSparkKafkaStream().select(
                functions.from_json(
                        functions.col("value").cast("string"),
                        Functions.getSchemaFromFile()
                ).alias("parsed_value"),
                functions.col("timestamp")
        ).select(
                functions.col("parsed_value.*"),
                functions.col("timestamp").as("event_arrived_time")
        );
    }
}
