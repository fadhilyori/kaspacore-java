package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;

import java.util.HashMap;
import java.util.Map;

public class StreamOutput implements StreamOutputInterface {
    private final Map<String, String> options;
    private String format;
    private String outputMode;

    public StreamOutput() {
        options = new HashMap<>();
        outputMode = "complete";
    }

    public String getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(String outputMode) {
        this.outputMode = outputMode;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOption(String key, String value) {
        this.options.put(key, value);
    }

    public DataStreamWriter<Row> runStream(Dataset<Row> rowDataset) {
        return rowDataset.writeStream()
                .outputMode(getOutputMode())
                .format(getFormat())
                .options(getOptions());
    }
}
