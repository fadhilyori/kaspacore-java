package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;

public interface StreamOutputInterface {
    DataStreamWriter<Row> runStream(Dataset<Row> rowDataset);
}
