package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.mataelang.kaspacore.utils.Functions;
import org.mataelang.kaspacore.utils.PropertyManager;

import java.util.Arrays;
import java.util.List;

public class Stream {
    public static void main(String[] args) throws Exception {
        List<String> agrEventCol = Arrays.asList("action", "class", "dir", "dst_addr", "dst_ap", "dst_port",
                "eth_dst", "eth_len", "eth_src", "eth_type", "gid", "iface", "ip_id", "ip_len", "msg",
                "mpls", "pkt_gen", "pkt_len", "pkt_num", "priority", "proto", "rev", "rule", "service",
                "sid", "src_addr", "src_ap", "src_port", "tcp_ack", "tcp_flags", "tcp_len", "tcp_seq",
                "tcp_win", "tos", "ttl", "vlan", "timestamp");

        List<String> agrTopIpCol = Arrays.asList("seconds", "action", "eth_src", "src_addr", "src_port");

        List<String> agrTopNetInfoCol = Arrays.asList("gid", "iface", "msg", "proto", "service", "vlan");

        SparkSession sparkSession = SparkSession
                .builder()
                .appName(PropertyManager.getInstance().getProperty("applicationName"))
                .master(PropertyManager.getInstance().getProperty("sparkMaster"))
                .config("spark.sql.session.timeZone", "Asia/Jakarta")
                .getOrCreate();

        Dataset<Row> rowDataset = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getInstance().getProperty("inputBootstrapServers"))
                .option("startingOffsets", PropertyManager.getInstance().getProperty("autoOffsetReset"))
                .option("subscribe", PropertyManager.getInstance().getProperty("inputTopic"))
                .load();

        StructType schema = new StructType(new StructField[]{
                new StructField("seconds", DataTypes.TimestampType, false, Metadata.empty()),
                new StructField("action", DataTypes.StringType, false, Metadata.empty()),
                new StructField("class", DataTypes.StringType, false, Metadata.empty()),
                new StructField("dir", DataTypes.StringType, false, Metadata.empty()),
                new StructField("dst_addr", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_ap", DataTypes.StringType, true, Metadata.empty()),
                new StructField("dst_port", DataTypes.IntegerType, true, Metadata.empty()),
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
                new StructField("tcp_ack", DataTypes.LongType, true, Metadata.empty()),
                new StructField("tcp_flags", DataTypes.StringType, true, Metadata.empty()),
                new StructField("tcp_len", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("tcp_seq", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("tcp_win", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("tos", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("ttl", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("vlan", DataTypes.IntegerType, false, Metadata.empty())
        });

        Dataset<Row> valueDF =
                rowDataset.select(
                        functions.from_json(functions.col("value").cast("string"), schema).alias(
                                "parsed_value"),
                        functions.col("timestamp")
                ).select(functions.col("parsed_value.*"), functions.col("timestamp"));

        // Event Example
        Dataset<Row> event30s = Functions.aggregate(valueDF, agrEventCol, "1 minute", "30 " +
                "seconds");

        Dataset<Row> eventTopIpAddr = Functions.aggregate(valueDF, agrTopIpCol, "1 minute", "30 seconds");

        Dataset<Row> eventNetInfo = Functions.aggregate(valueDF, agrTopNetInfoCol, "1 minute", "30 seconds");

        eventNetInfo.writeStream()
                .outputMode("complete")
                .format("console")
                .option("truncate", false)
                .start()
                .awaitTermination();
    }
}
