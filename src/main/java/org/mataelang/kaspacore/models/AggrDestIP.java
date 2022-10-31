package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrDestIP extends AggregationModel{
    public AggrDestIP() {
        fields = Arrays.asList("action", "eth_dst", "sensor_id");
        nullableFields = Arrays.asList("dst_addr", "dst_port", "dst_country_code",
                "dst_country_name", "dst_city_name", "dst_long", "dst_lat");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "destination_ip_address_10s";
    }
}
