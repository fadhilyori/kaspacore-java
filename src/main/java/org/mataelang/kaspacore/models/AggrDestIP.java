package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrDestIP extends AggregationModel{
    public AggrDestIP() {
        fields = Arrays.asList(
                "action",
                "dst_addr",
                "dst_country_code",
                "dst_country_name",
                "dst_location",
                "dst_port",
                "eth_dst",
                "sensor_id"
        );
        delayThreshold = "5 seconds";
        windowDuration = "10 seconds";
        topic = "destination_ip_address_10s";
    }
}
