package org.mataelang.kaspacore.models;

import java.util.Arrays;

public class AggrEvent extends AggregationModel {
    public AggrEvent() {
        fields = Arrays.asList("action", "class", "dir", "dst_addr", "dst_port",
                "eth_dst", "eth_len", "eth_src", "eth_type", "gid", "iface", "ip_id", "ip_len", "msg",
                "mpls", "pkt_gen", "pkt_len", "pkt_num", "priority", "proto", "rev", "service",
                "sid", "src_addr", "src_port", "tcp_ack", "tcp_flags", "tcp_len", "tcp_seq",
                "tcp_win", "tos", "ttl", "vlan", "sensor_id");
        delayThreshold = "1 minute";
        windowDuration = "10 seconds";
        topic = "event_" + windowDuration.replace(" ", "_");
    }
}
