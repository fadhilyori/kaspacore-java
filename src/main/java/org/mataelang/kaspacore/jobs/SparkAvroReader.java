package org.mataelang.kaspacore.jobs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.mataelang.kaspacore.models.AggrAlertInfo;
import org.mataelang.kaspacore.models.AggrDestIP;
import org.mataelang.kaspacore.models.AggrEvent;
import org.mataelang.kaspacore.models.AggrSourceIP;
import org.mataelang.kaspacore.outputs.ConsoleOutput;
import org.mataelang.kaspacore.outputs.StreamOutputInterface;
import org.mataelang.kaspacore.providers.Spark;

import java.util.concurrent.TimeoutException;

public class SparkAvroReader {
    public static void main(String[] args) throws InterruptedException, TimeoutException, StreamingQueryException {

        Dataset<Row> rowDataset = Spark.getSparkKafkaStreamParsedAvro();

        // List of jobs
        StreamOutputInterface output = new ConsoleOutput();
        output.runStream(rowDataset);
//        Spark.job(rowDataset, new AggrEvent()).start();

        Spark.getSparkSession().streams().awaitAnyTermination();
    }
}
