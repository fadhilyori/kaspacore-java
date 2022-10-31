package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrAlertInfo extends AggregationModel {
    public AggrAlertInfo() {
        fields = Arrays.asList("msg", "proto", "class", "priority", "sensor_id");
        nullableFields = Arrays.asList("src_addr", "dst_addr");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "alert_information_10s";
    }
}
