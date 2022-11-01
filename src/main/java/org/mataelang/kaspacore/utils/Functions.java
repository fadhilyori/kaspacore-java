package org.mataelang.kaspacore.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
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
import org.mataelang.kaspacore.schemas.EventSchema;
import scala.jdk.CollectionConverters;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    private Functions() {
    }

    public static DataStreamWriter<Row> aggregateStream(Dataset<Row> rowDataset, AggregationModel className) {

        String timeColumn = "seconds";
        String windowStartColumnName = "seconds";
        StreamOutputInterface streamOutputInterface = new KafkaOutput(className.getTopic());

        Dataset<Row> windowedCount = rowDataset
                .withWatermark(timeColumn, className.getDelayThreshold())
                .groupBy(
                        CollectionConverters.IteratorHasAsScala(
                                addGetColumn(
                                        className.getFields(),
                                        functions.window(rowDataset.col(timeColumn), className.getWindowDuration())
                                ).iterator()
                        ).asScala().toSeq()
                ).count();

        Dataset<Row> selectedField = windowedCount.select(
                CollectionConverters.IteratorHasAsScala(addGetColumn(
                        className.getFields(),
                        Arrays.asList(functions.col("window.start").alias(windowStartColumnName),
                                functions.col("count")
                        ))
                        .iterator()).asScala().toSeq()
        );

        return streamOutputInterface.runStream(selectedField);
    }

    private static List<Column> addGetColumn(List<String> fields, Column newColumn) {
        List<Column> newFields = fields.stream().map(Column::new).collect(Collectors.toList());
        newFields.add(newColumn);

        return newFields;
    }

    private static List<Column> addGetColumn(List<String> fields, List<Column> newColumn) {

        List<Column> newFields = fields.stream().map(Column::new).collect(Collectors.toList());
        newFields.addAll(newColumn);

        return newFields;
    }

    public static StructType getSchemaFromFile() {
        URI uri;
        String content;

        try {
            uri = ClassLoader.getSystemClassLoader().getResource("event_schema.json").toURI();
            content = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new KaspaCoreRuntimeException(e);
        }

        Logger.getLogger(EventSchema.class).error(content);
        return (StructType) DataType.fromJson(content);
    }
}
