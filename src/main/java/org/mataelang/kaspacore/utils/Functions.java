package org.mataelang.kaspacore.utils;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.mataelang.kaspacore.models.AggregationModel;
import scala.jdk.CollectionConverters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    private Functions() {}

    public static Dataset<Row> aggregate(Dataset<Row> rowDataset, AggregationModel className) {

        String timeColumn = "seconds";
        String windowStartColumnName = "seconds";
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

        return windowedCount.select(
                CollectionConverters.IteratorHasAsScala(addGetColumn(
                        className.getFields(),
                        Arrays.asList(functions.col("window.start").alias(windowStartColumnName),
                                functions.col("count")
                        ))
                        .iterator()).asScala().toSeq()
        );
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
}
