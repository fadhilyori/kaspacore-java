package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrSourceIP extends AggregationModel {
    public AggrSourceIP() {
        fields = Arrays.asList(
                "action",
                "eth_src",
                "sensor_id",
                "src_addr",
                "src_country_code",
                "src_country_name",
                "src_location",
                "src_port"
        );
        delayThreshold = "5 seconds";
        windowDuration = "10 seconds";
        topic = "source_ip_address_10s";
    }
}
