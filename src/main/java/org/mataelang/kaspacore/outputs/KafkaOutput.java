package org.mataelang.kaspacore.outputs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.mataelang.kaspacore.utils.PropertyManager;

public class KafkaOutput extends StreamOutput {
    public KafkaOutput(String topic) {
        setOption("kafka.bootstrap.servers", PropertyManager.getProperty("KAFKA_BOOTSTRAP_SERVERS"));
        setFormat("kafka");
        setOption("topic", topic);
        setOutputMode("update");
    }

    @Override
    public DataStreamWriter<Row> runStream(Dataset<Row> rowDataset) {
        return super.runStream(rowDataset.selectExpr("to_json(struct(*)) AS value"));
    }
}
