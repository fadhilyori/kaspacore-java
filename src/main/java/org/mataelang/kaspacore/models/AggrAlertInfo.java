package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrAlertInfo extends AggregationModel {
    public AggrAlertInfo() {
        fields = Arrays.asList(
                "class",
                "msg",
                "priority",
                "gid",
                "rev",
                "sid",
                "service",
                "proto",
                "sensor_id"
        );
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "alert_information_10s";
    }
}
