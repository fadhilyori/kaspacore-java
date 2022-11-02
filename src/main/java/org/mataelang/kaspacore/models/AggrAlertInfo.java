package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrAlertInfo extends AggregationModel {
    public AggrAlertInfo() {
        fields = Arrays.asList(
                "class",
                "dst_addr",
                "msg",
                "priority",
                "proto",
                "sensor_id",
                "src_addr"
        );
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "alert_information_10s";
    }
}
