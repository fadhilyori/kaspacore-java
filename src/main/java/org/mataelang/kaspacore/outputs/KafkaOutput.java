package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.HashMap;
import java.util.Map;

public class KafkaOutput implements StreamOutputInterface {
    private final String format = "kafka";
    private final Map<String, String> config = new HashMap<>();

    public KafkaOutput() {
        config.put("kafka.bootstrap.servers", PropertyManager.getProperty("KAFKA_BOOTSTRAP_SERVERS"));
    }

    @Override
    public DataStreamWriter<Row> runStream(Dataset<Row> rowDataset, String topic) {
        config.put("topic", topic);
        return rowDataset.writeStream()
                .format(this.format)
                .options(config);
    }
}
