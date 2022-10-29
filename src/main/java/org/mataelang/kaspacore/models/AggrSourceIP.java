package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrSourceIP extends AggregationModel{
    public AggrSourceIP() {
        fields = Arrays.asList("action", "eth_src", "sensor_id");
        nullableFields = Arrays.asList("src_addr", "src_port", "src_country_code",
                "src_country_name", "src_long", "src_lat");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
    }
}
