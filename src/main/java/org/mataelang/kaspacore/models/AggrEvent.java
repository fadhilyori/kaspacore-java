package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrEvent extends AggregationModel {
    public AggrEvent() {
        fields = Arrays.asList(
                "action",
                "b64_data",
                "class",
                "dir",
                "dst_addr",
                "dst_country_code",
                "dst_country_name",
                "dst_location",
                "dst_port",
                "eth_dst",
                "eth_len",
                "eth_src",
                "eth_type",
                "gid",
                "iface",
                "ip_id",
                "ip_len",
                "mpls",
                "msg",
                "pkt_gen",
                "pkt_len",
                "pkt_num",
                "priority",
                "proto",
                "rev",
                "rule",
                "sensor_id",
                "service",
                "sid",
                "src_addr",
                "src_country_code",
                "src_country_name",
                "src_location",
                "src_port",
                "tcp_ack",
                "tcp_flags",
                "tcp_len",
                "tcp_seq",
                "tcp_win",
                "tos",
                "ttl",
                "vlan"
        );
        delayThreshold = "5 seconds";
        windowDuration = "10 seconds";
        topic = "event_all_10s";
        dropRowIfNull = false;
    }
}
