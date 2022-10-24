package org.mataelang.kaspacore.utils;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import scala.jdk.CollectionConverters;

import java.util.List;

public class Functions {
    public static Dataset<Row> aggregate(Dataset<Row> rowDataset, List<String> fields, String delayThreshold,
                                         String windowDuration) {
        Dataset<Row> windowedCount = rowDataset
                .withWatermark("seconds", delayThreshold)
                .groupBy(
                        functions.window(rowDataset.col("seconds"), windowDuration).toString(),
                        CollectionConverters.IteratorHasAsScala(fields.iterator()).asScala().toSeq()
                ).count();

        return windowedCount.select(
                functions.col("window.start").alias("seconds").toString(),
                CollectionConverters.IteratorHasAsScala(fields.iterator()).asScala().toSeq()
        );
    }
}
