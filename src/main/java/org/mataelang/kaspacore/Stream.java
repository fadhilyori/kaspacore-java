package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.outputs.ConsoleOutput;
import org.mataelang.kaspacore.outputs.StreamOutput;
import org.mataelang.kaspacore.providers.Spark;
import org.mataelang.kaspacore.utils.Functions;

public class Stream {
    public static void main(String[] args) throws Exception {
        StreamOutput output = new ConsoleOutput();
        Dataset<Row> rowDataset = Spark.getSparkKafkaStreamParsed();

        Functions.aggregateStream(rowDataset, new AggrSourceIP(), output)
                .start().awaitTermination();

    }
}
