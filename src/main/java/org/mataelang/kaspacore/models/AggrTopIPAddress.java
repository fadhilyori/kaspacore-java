package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrTopIPAddress extends AggregationModel {
    public AggrTopIPAddress() {
        fields = Arrays.asList("seconds", "action", "eth_src", "src_addr", "src_port");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
    }
}
