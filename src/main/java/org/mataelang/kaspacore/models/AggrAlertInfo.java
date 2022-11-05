package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrAlertInfo extends AggregationModel {
    public AggrAlertInfo() {
        fields = Arrays.asList(
                "class",
                "gid",
                "msg",
                "priority",
                "proto",
                "rev",
                "sensor_id",
                "service",
                "sid"
        );
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "alert_information_10s";
    }
}
