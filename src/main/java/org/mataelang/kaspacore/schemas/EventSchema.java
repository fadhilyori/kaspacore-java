package org.mataelang.kaspacore.schemas;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class EventSchema {
    private EventSchema() {}

    public static StructType getSchema() {
        StructType location = new StructType(new StructField[]{
                new StructField("lat", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("lon", DataTypes.DoubleType, true, Metadata.empty()),
        });

        return new StructType(new StructField[]{
                new StructField("action", DataTypes.StringType, true, Metadata.empty()),
                new StructField("b64_data", DataTypes.StringType, true, Metadata.empty()),
                new StructField("class", DataTypes.StringType, true, Metadata.empty()),
                new StructField("client_bytes", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("client_pkts", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("dir", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_addr", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_ap", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_port", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("dst_country_code", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_country_name", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_location", location, true, Metadata.empty()),
                new StructField("eth_dst", DataTypes.StringType, true, Metadata.empty()),
                new StructField("eth_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("eth_src", DataTypes.StringType, true, Metadata.empty()),
                new StructField("eth_type", DataTypes.StringType, true, Metadata.empty()),
                new StructField("flowstart_time", DataTypes.TimestampType, true, Metadata.empty()),
                new StructField("geneve_vni", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("gid", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("icmp_code", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("icmp_id", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("icmp_seq", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("icmp_type", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("iface", DataTypes.StringType, true, Metadata.empty()),
                new StructField("ip_id", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("ip_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("mpls", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("msg", DataTypes.StringType, true, Metadata.empty()),
                new StructField("pkt_gen", DataTypes.StringType, true, Metadata.empty()),
                new StructField("pkt_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("pkt_num", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("priority", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("proto", DataTypes.StringType, true, Metadata.empty()),
                new StructField("rev", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("rule", DataTypes.StringType, true, Metadata.empty()),
                new StructField("seconds", DataTypes.TimestampType, true, Metadata.empty()),
                new StructField("sensor_id", DataTypes.StringType, true, Metadata.empty()),
                new StructField("server_bytes", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("server_pkts", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("service", DataTypes.StringType, true, Metadata.empty()),
                new StructField("sgt", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("sid", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("src_addr", DataTypes.StringType, true, Metadata.empty()),
                new StructField("src_ap", DataTypes.StringType, true, Metadata.empty()),
                new StructField("src_port", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("src_country_code", DataTypes.StringType, true, Metadata.empty()),
                new StructField("src_country_name", DataTypes.StringType, true, Metadata.empty()),
                new StructField("src_location", location, true, Metadata.empty()),
                new StructField("target", DataTypes.LongType, true, Metadata.empty()),
                new StructField("tcp_ack", DataTypes.LongType, true, Metadata.empty()),
                new StructField("tcp_flags", DataTypes.StringType, true, Metadata.empty()),
                new StructField("tcp_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("tcp_seq", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("tcp_win", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("timestamp", DataTypes.TimestampType, true, Metadata.empty()),
                new StructField("tos", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("ttl", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("udp_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("vlan", DataTypes.IntegerType, true, Metadata.empty()),
        });
    }
}
