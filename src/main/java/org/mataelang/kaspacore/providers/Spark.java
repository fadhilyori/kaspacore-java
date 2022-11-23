package org.mataelang.kaspacore.providers;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.jetbrains.annotations.NotNull;
import org.mataelang.kaspacore.models.AggregationModel;
import org.mataelang.kaspacore.outputs.KafkaOutput;
import org.mataelang.kaspacore.outputs.StreamOutputInterface;
import org.mataelang.kaspacore.schemas.EventSchema;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Spark {
    private static JavaStreamingContext streamingContext;

    private Spark() {
    }

    private static SparkConf getSparkConf(String appName) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(appName);
        sparkConf.setMaster(PropertyManager.getProperty("SPARK_MASTER"));
        sparkConf.set("spark.sql.streaming.checkpointLocation", PropertyManager.getProperty("SPARK_CHECKPOINT_PATH"));
        sparkConf.set("spark.sql.session.timeZone", PropertyManager.getProperty("TIMEZONE", "UTC"));
        sparkConf.validateSettings();

        return sparkConf;
    }

    public static JavaStreamingContext getStreamingContext(String appName) {
        if (streamingContext == null) {
            streamingContext = new JavaStreamingContext(getSparkConf(appName), Durations.seconds(1));
        }

        streamingContext.sparkContext().addFile(PropertyManager.getProperty("MAXMIND_DB_PATH"));

        return streamingContext;
    }

    public static SparkSession getSparkSession(String appName) {
        return SparkSession
                .builder()
                .config(getSparkConf(appName))
                .getOrCreate();
    }

    public static Dataset<Row> getKafkaStreamDataset(SparkSession sparkSession) {
        return sparkSession.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getProperty("KAFKA_BOOTSTRAP_SERVERS"))
                .option("startingOffsets", PropertyManager.getProperty("KAFKA_INPUT_STARTING_OFFSETS", "latest"))
                .option("subscribe", PropertyManager.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"))
                .load();
    }

    public static Dataset<Row> getParsedDataset(SparkSession sparkSession) {
        return Spark.getKafkaStreamDataset(sparkSession).select(
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

    /**
     * @param model Model used to define the aggregation
     * @return Return DataStreamWriter, required manually call `.start()` to start the stream
     */
    public static DataStreamWriter<Row> job(Dataset<Row> rowDataset, @NotNull AggregationModel model) {
        StreamOutputInterface streamOutputInterface = new KafkaOutput(model.getTopic());

        rowDataset = model.aggregate(rowDataset);

        return streamOutputInterface.runStream(rowDataset);
    }
}
