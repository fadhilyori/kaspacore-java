package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;

public class ConsoleOutput extends StreamOutput {
    private final String outputMode;

    public ConsoleOutput() {
        this.outputMode = "complete";
        configure(false);
    }

    public ConsoleOutput(String outputMode) {
        this.outputMode = outputMode;
        configure(false);
    }

    public ConsoleOutput(String outputMode, Boolean truncate) {
        this.outputMode = outputMode;
        configure(truncate);
    }

    private void configure(Boolean truncate) {
        setOption("truncate", String.valueOf(truncate));
        setFormat("console");
    }

    @Override
    public DataStreamWriter<Row> runStream(Dataset<Row> rowDataset) {
        return super.runStream(rowDataset).outputMode(this.outputMode);
    }
}
