package org.mataelang.kaspacore.models;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import scala.collection.immutable.Seq;
import scala.jdk.CollectionConverters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AggregationModel {
    protected List<String> fields;
    protected List<Column> columnList;
    protected List<String> nullableFields;
    protected String delayThreshold;
    protected String windowDuration;
    protected String topic;
    protected Boolean dropRowIfNull;
    protected String timeColumn;
    protected String newTimeColumnName;

    public AggregationModel() {
        timeColumn = "seconds";
        newTimeColumnName = "@timestamp";
        fields = null;
        nullableFields = null;
        delayThreshold = "1 minute";
        windowDuration = "60 seconds";
        topic = null;
        dropRowIfNull = true;
        columnList = null;
    }

    public void setColumnList() {
        this.columnList = getFieldAsColumn();
    }

    public String getTopic() {
        return topic;
    }

    private Column getWindowColumn() {
        return functions.window(functions.col(timeColumn), windowDuration);
    }

    public void addColumn(Column column) {
        if (columnList == null) {
            setColumnList();
        }

        columnList.add(column);
    }

    public List<Column> getFieldAsColumn() {
        if (fields != null) {
            return fields.stream().map(Column::new).collect(Collectors.toCollection(ArrayList<Column>::new));
        }

        return Collections.emptyList();
    }

    public Seq<Column> getAsSeqColumn() {
        return CollectionConverters.IteratorHasAsScala(columnList.iterator()).asScala().toSeq();
    }

    public Seq<String> getAsSeqString() {
        return CollectionConverters.IteratorHasAsScala(fields.iterator()).asScala().toSeq();
    }

    public Dataset<Row> aggregate(Dataset<Row> dataset) {
        if (Boolean.TRUE.equals(dropRowIfNull)) {
            dataset = dataset.na().drop(getAsSeqString());
        }

        addColumn(getWindowColumn());

        Dataset<Row> aggrCountDataset = dataset
                .withWatermark(timeColumn, delayThreshold)
                .groupBy(getAsSeqColumn())
                .count();

        return aggrCountDataset
                .withColumn(newTimeColumnName, aggrCountDataset.col("window.start"))
                .drop("window");
    }
}
