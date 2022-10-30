package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.outputs.ConsoleOutput;
import org.mataelang.kaspacore.outputs.StreamOutput;
import org.mataelang.kaspacore.schemas.EventSchema;
import org.mataelang.kaspacore.utils.Functions;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Stream {
    public static void main(String[] args) throws Exception {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName(PropertyManager.getInstance().getProperty("applicationName"))
                .master(PropertyManager.getInstance().getProperty("sparkMaster"))
                .config("spark.sql.session.timeZone", "Asia/Jakarta")
                .getOrCreate();

        StreamOutput output = new ConsoleOutput();

        Dataset<Row> rowDataset = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getInstance().getProperty("inputBootstrapServers"))
                .option("startingOffsets", PropertyManager.getInstance().getProperty("autoOffsetReset"))
                .option("subscribe", PropertyManager.getInstance().getProperty("outputTopic"))
                .load();

        Dataset<Row> valueDF =
                rowDataset.select(
                        functions.from_json(functions.col("value").cast("string"), EventSchema.getSchema()).alias(
                                "parsed_value"),
                        functions.col("timestamp")
                ).select(functions.col("parsed_value.*"), functions.col("timestamp"));

        Functions.aggregateStream(valueDF, new AggrSourceIP(), output)
                .start().awaitTermination();

    }
}
