package org.mataelang.kaspacore.schemas;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.spark.sql.types.*;
import org.mataelang.kaspacore.exceptions.KaspaCoreRuntimeException;
import org.mataelang.kaspacore.providers.Spark;
import scala.io.Source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EventSchema {
    private static final StructType schema = new StructType(new StructField[]{
            new StructField("seconds", DataTypes.TimestampType, false, Metadata.empty()),
            new StructField("action", DataTypes.StringType, false, Metadata.empty()),
            new StructField("class", DataTypes.StringType, false, Metadata.empty()),
            new StructField("dir", DataTypes.StringType, false, Metadata.empty()),
            new StructField("dst_addr", DataTypes.StringType, true, Metadata.empty()),
            new StructField("dst_ap", DataTypes.StringType, true, Metadata.empty()),
            new StructField("dst_port", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("dst_country_code", DataTypes.StringType, true, Metadata.empty()),
            new StructField("dst_country_name", DataTypes.StringType, true, Metadata.empty()),
            new StructField("eth_dst", DataTypes.StringType, true, Metadata.empty()),
            new StructField("eth_len", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("eth_src", DataTypes.StringType, true, Metadata.empty()),
            new StructField("eth_type", DataTypes.StringType, true, Metadata.empty()),
            new StructField("gid", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("iface", DataTypes.StringType, false, Metadata.empty()),
            new StructField("ip_id", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("ip_len", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("msg", DataTypes.StringType, false, Metadata.empty()),
            new StructField("mpls", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("pkt_gen", DataTypes.StringType, false, Metadata.empty()),
            new StructField("pkt_len", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("pkt_num", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("priority", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("proto", DataTypes.StringType, false, Metadata.empty()),
            new StructField("rev", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("rule", DataTypes.StringType, false, Metadata.empty()),
            new StructField("service", DataTypes.StringType, false, Metadata.empty()),
            new StructField("sid", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("src_addr", DataTypes.StringType, true, Metadata.empty()),
            new StructField("src_ap", DataTypes.StringType, true, Metadata.empty()),
            new StructField("src_port", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("src_country_code", DataTypes.StringType, true, Metadata.empty()),
            new StructField("src_country_name", DataTypes.StringType, true, Metadata.empty()),
            new StructField("tcp_ack", DataTypes.LongType, true, Metadata.empty()),
            new StructField("tcp_flags", DataTypes.StringType, true, Metadata.empty()),
            new StructField("tcp_len", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("tcp_seq", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("tcp_win", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("tos", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("ttl", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("vlan", DataTypes.IntegerType, false, Metadata.empty()),
            new StructField("sensor_id", DataTypes.StringType, false, Metadata.empty()),
    });

    private EventSchema() {}

    public static StructType getSchema() {
        return schema;
    }
}
