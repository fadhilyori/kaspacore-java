package org.mataelang.kaspacore.jobs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.mataelang.kaspacore.models.AggrAlertInfo;
import org.mataelang.kaspacore.models.AggrDestIP;
import org.mataelang.kaspacore.models.AggrEvent;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.providers.Spark;

import static org.mataelang.kaspacore.utils.Functions.aggregateStream;

public class SensorAggregationStreamJob {
    public static void main(String[] args) throws Exception {
        Dataset<Row> rowDataset = Spark.getSparkKafkaStreamParsed();

        // List of jobs
        aggregateStream(rowDataset, new AggrAlertInfo())
                .start();

        aggregateStream(rowDataset, new AggrSourceIP())
                .start();

        aggregateStream(rowDataset, new AggrDestIP())
                .start();

        aggregateStream(rowDataset, new AggrEvent())
                .start();

        Spark.getSparkSession().streams().awaitAnyTermination();
    }
}
