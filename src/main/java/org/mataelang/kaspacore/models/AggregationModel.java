package org.mataelang.kaspacore.models;

import java.util.Iterator;
import java.util.List;

public class AggregationModel {
    protected List<String> fields;
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
    }

    public Boolean getDropRowIfNull() {
        return dropRowIfNull;
    }

    public List<String> getFields() {
        return fields;
    }

    public Iterator<String> getFieldsIterator() {
        return fields.iterator();
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
}
