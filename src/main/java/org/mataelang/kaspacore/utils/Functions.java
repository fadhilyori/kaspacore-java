package org.mataelang.kaspacore.utils;

import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.mataelang.kaspacore.exceptions.KaspaCoreRuntimeException;
import org.mataelang.kaspacore.models.AggregationModel;
import org.mataelang.kaspacore.outputs.KafkaOutput;
import org.mataelang.kaspacore.outputs.StreamOutputInterface;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Functions {
    private Functions() {
    }

    public static DataStreamWriter<Row> aggregateStream(Dataset<Row> rowDataset, AggregationModel model) {

        String timeColumn = "seconds";
        String windowStartColumnName = "@timestamp";
        StreamOutputInterface streamOutputInterface = new KafkaOutput(model.getTopic());
        Column windowColumn = functions.window(rowDataset.col(timeColumn), model.getWindowDuration());

        if (Boolean.TRUE.equals(model.getDropRowIfNull())) {
            rowDataset = rowDataset.na().drop(model.getAsSeqString());
        }

        model.addColumn(windowColumn);

        Dataset<Row> windowedCount = rowDataset
                .withWatermark(timeColumn, model.getDelayThreshold())
                .groupBy(model.getAsSeqColumn())
                .count();

        Dataset<Row> filteredDataSet = windowedCount
                .withColumn(windowStartColumnName, windowedCount.col("window.start"))
                .drop("window");

        return streamOutputInterface.runStream(filteredDataSet);
    }

    public static StructType getSchemaFromFile() {
        URI uri;
        String content;

        try {
            uri = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("event_schema.json")).toURI();
            content = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new KaspaCoreRuntimeException(e);
        }

        return (StructType) DataType.fromJson(content);
    }
}
