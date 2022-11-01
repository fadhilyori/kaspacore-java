package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.providers.Spark;
import static org.mataelang.kaspacore.utils.Functions.aggregateStream;

public class Stream {
    public static void main(String[] args) throws Exception {
        Dataset<Row> rowDataset = Spark.getSparkKafkaStreamParsed();

        // List of jobs
        aggregateStream(rowDataset, new AggrSourceIP())
                .start().awaitTermination();

    }
}
