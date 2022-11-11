package org.mataelang.kaspacore.providers;

import org.apache.avro.Schema;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.DataStreamWriter;
import static org.apache.spark.sql.avro.functions.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.jetbrains.annotations.NotNull;
import org.mataelang.kaspacore.models.AggregationModel;
import org.mataelang.kaspacore.outputs.KafkaOutput;
import org.mataelang.kaspacore.outputs.StreamOutputInterface;
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
        sparkConf.set("spark.sql.session.timeZone", PropertyManager.getProperty("TIMEZONE", "UTC"));
        sparkConf.validateSettings();

        return sparkConf;
    }

    public static JavaStreamingContext getStreamingContext() {
        if (streamingContext == null) {
            streamingContext = new JavaStreamingContext(getSparkConf(), Durations.seconds(1));
        }

        streamingContext.sparkContext().addFile(PropertyManager.getProperty("MAXMIND_DB_PATH"));

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

    public static Dataset<Row> getSparkKafkaStreamParsedAvro() {
        return Spark.getSparkKafkaStream().select(
                from_avro(
                        functions.col("value"),
                        Functions.getAvroSchemaFromFile()
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
