package org.mataelang.kaspacore.jobs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mataelang.kaspacore.models.AggrAlertInfo;
import org.mataelang.kaspacore.models.AggrDestIP;
import org.mataelang.kaspacore.models.AggrEvent;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.providers.Spark;

public class SensorAggregationStreamJob {
    public static void main(String[] args) throws Exception {
        String appName = "SensorAggregationStream";

        SparkSession sparkSession = Spark.getSparkSession(appName);

        Dataset<Row> rowDataset = Spark.getParsedDataset(sparkSession);

        // List of jobs
        Spark.job(rowDataset, new AggrEvent()).start();
        Spark.job(rowDataset, new AggrAlertInfo()).start();
        Spark.job(rowDataset, new AggrSourceIP()).start();
        Spark.job(rowDataset, new AggrDestIP()).start();

        sparkSession.streams().awaitAnyTermination();
    }
}
