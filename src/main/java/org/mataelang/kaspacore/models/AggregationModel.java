package org.mataelang.kaspacore.models;

import java.util.List;

public class AggregationModel {
    protected List<String> fields;
    protected String delayThreshold;
    protected String windowDuration;

    public AggregationModel() {
        fields = null;
        delayThreshold = "1 minute";
        windowDuration = "30 seconds";
    }

    public List<String> getFields() {
        return fields;
    }
    public String getDelayThreshold() { return delayThreshold; }
    public String getWindowDuration() { return windowDuration; }
}
