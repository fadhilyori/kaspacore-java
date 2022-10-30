package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;

public class ConsoleOutput implements StreamOutputInterface {

    private final String format = "console";
    private final String outputMode;
    private final boolean truncate;

    public ConsoleOutput() {
        this.outputMode = "complete";
        this.truncate = false;
    }

    public ConsoleOutput(String outputMode) {
        this.outputMode = outputMode;
        this.truncate = false;
    }

    public ConsoleOutput(String outputMode, Boolean truncate) {
        this.outputMode = outputMode;
        this.truncate = truncate;
    }

    @Override
    public DataStreamWriter<Row> runStream(Dataset<Row> rowDataset, String topic) {
        return rowDataset.writeStream()
                .outputMode(this.outputMode)
                .format(this.format)
                .option("truncate", this.truncate);
    }
}
