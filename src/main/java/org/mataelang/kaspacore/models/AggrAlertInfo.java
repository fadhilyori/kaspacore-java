package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrAlertInfo extends AggregationModel {
    public AggrAlertInfo() {
        fields = Arrays.asList("msg", "proto", "class", "priority", "sensor_id", "src_addr", "dst_addr");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
    }
}
