package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;

import java.util.HashMap;
import java.util.Map;

public class StreamOutput implements StreamOutputInterface {
    private final Map<String, String> options;
    private String format;

    public StreamOutput() {
        options = new HashMap<>();
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
                .format(getFormat())
                .options(getOptions());
    }
}
