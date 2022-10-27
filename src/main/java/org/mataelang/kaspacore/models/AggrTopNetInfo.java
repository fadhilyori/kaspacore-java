package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrTopNetInfo extends AggregationModel {
    public AggrTopNetInfo() {
        fields = Arrays.asList("gid", "iface", "msg", "proto", "service", "vlan");
        delayThreshold = "1 minute";
        windowDuration = "30 seconds";
    }
}
