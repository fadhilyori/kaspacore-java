package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Stream {
    public static void main(String[] args) throws Exception {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName(PropertyManager.getInstance().getProperty("applicationName"))
                .master(PropertyManager.getInstance().getProperty("sparkMaster"))
                .config("spark.sql.session.timeZone", "Asia/Jakarta")
                .getOrCreate();

        Dataset<Row> rowDataset = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getInstance().getProperty("inputBootstrapServers"))
                .option("startingOffsets", PropertyManager.getInstance().getProperty("autoOffsetReset"))
                .option("subscribe", PropertyManager.getInstance().getProperty("inputTopic"))
                .load();

        StructType schema = new StructType(new StructField[] {
                new StructField("seconds", DataTypes.TimestampType, false, Metadata.empty()),
                new StructField("name", DataTypes.StringType, true, Metadata.empty()),
                new StructField("b64_data", DataTypes.StringType, true, Metadata.empty())
        });

        Dataset<Row> valueDF =
                rowDataset.select(
                        functions.from_json(functions.col("value").cast("string"), schema).alias(
                        "parsed_value"),
                        functions.col("timestamp")
                ).select(functions.col("parsed_value.*"), functions.col("timestamp"));

//        Dataset<Person> personDataset = rowDataset.as(ExpressionEncoder.javaBean(Person.class));

        Dataset<Row> windowedCount = valueDF
                .withWatermark("seconds", "1 minutes")
                .groupBy(
                        functions.window(valueDF.col("seconds"), "30 seconds"),
//                        functions.session_window(functions.col("seconds"), "1 minute"),
//                        valueDF.col("name"),
                        valueDF.col("src_ip"),
                        valueDF.col("sensor_id")
//                        valueDF.col("age")
                ).count();

        Dataset<Row> event10s = windowedCount.select(
                functions.col("window.start").alias("seconds"),
                functions.col("name"),
                functions.col("count")
        );

        event10s.writeStream()
                .outputMode("complete")
                .format("console")
                .option("truncate", false)
                .start()
                .awaitTermination();
    }
}
