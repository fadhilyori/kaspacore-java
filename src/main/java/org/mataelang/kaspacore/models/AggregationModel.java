package org.mataelang.kaspacore.models;

import org.apache.spark.sql.Column;
import scala.collection.immutable.Seq;
import scala.jdk.CollectionConverters;

import java.util.Collections;
import java.util.List;

public class AggregationModel {
    protected List<String> fields;
    protected List<Column> columnList;
    protected List<String> nullableFields;
    protected String delayThreshold;
    protected String windowDuration;
    protected String topic;
    protected Boolean dropRowIfNull;

    public AggregationModel() {
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

    public Boolean getDropRowIfNull() {
        return dropRowIfNull;
    }

    public List<String> getFields() {
        return fields;
    }

    public String getDelayThreshold() {
        return delayThreshold;
    }

    public String getWindowDuration() {
        return windowDuration;
    }

    public String getTopic() {
        return topic;
    }

    public void addColumn(Column column) {
        if (columnList == null) {
            setColumnList();
        }

        columnList.add(column);
    }

    public List<Column> getFieldAsColumn() {
        if (fields != null) {
            return fields.stream().map(Column::new).toList();
        }

        return Collections.emptyList();
    }

    public Seq<Column> getAsSeqColumn() {
        return CollectionConverters.IteratorHasAsScala(columnList.iterator()).asScala().toSeq();
    }

    public Seq<String> getAsSeqString() {
        return CollectionConverters.IteratorHasAsScala(fields.iterator()).asScala().toSeq();
    }
}
