package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrSourceIP extends AggregationModel{
    public AggrSourceIP() {
        fields = Arrays.asList("action", "eth_src", "src_addr", "src_port", "src_country_code",
                "src_country_name", "src_long", "src_lat", "sensor_id");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "source_ip_address_10s";
    }
}
