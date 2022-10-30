package org.mataelang.kaspacore;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Producer;
import org.mataelang.kaspacore.providers.Spark;
import org.mataelang.kaspacore.utils.IPLookupTool;
import org.mataelang.kaspacore.utils.PropertyManager;

public class DataStream {
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger(DataStream.class).setLevel(
                Level.toLevel(
                        PropertyManager.getProperty("LOG_LEVEL"),
                        Level.DEBUG
                )
        );

        Consumer.getInstance().getStream(Spark.getStreamingContext()).foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
                Producer.getInstance().connect();

//                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];

                // send to kafka
                recordIterator.forEachRemaining(message -> {
                    ObjectNode node = message.value().deepCopy();
                    JsonNode srcAddrNode = node.get("src_addr");
                    JsonNode dstAddrNode = node.get("dst_addr");

                    if (srcAddrNode != null) {
                        String srcAddr = srcAddrNode.textValue();
                        CityResponse srcAddrGeoIPDetail = IPLookupTool.getInstance().get(srcAddr);

                        if (srcAddrGeoIPDetail != null) {
                            Country country = srcAddrGeoIPDetail.getCountry();
                            City city = srcAddrGeoIPDetail.getCity();
                            Location location = srcAddrGeoIPDetail.getLocation();
                            node.put("src_country_code", country.getIsoCode());
                            node.put("src_country_name", country.getName());
                            if (city.getName() != null) {
                                node.put("src_city_name", city.getName());
                            }
                            node.put("src_long", location.getLongitude());
                            node.put("src_lat", location.getLatitude());
                        }
                    }

                    if (dstAddrNode != null) {
                        String dstAddr = dstAddrNode.textValue();
                        CityResponse dstAddrGeoIPDetail = IPLookupTool.getInstance().get(dstAddr);

                        if (dstAddrGeoIPDetail != null) {
                            Country country = dstAddrGeoIPDetail.getCountry();
                            City city = dstAddrGeoIPDetail.getCity();
                            Location location = dstAddrGeoIPDetail.getLocation();
                            node.put("dst_country_code", country.getIsoCode());
                            node.put("dst_country_name", country.getName());
                            if (city.getName() != null) {
                                node.put("dst_city_name", city.getName());
                            }
                            node.put("dst_long", location.getLongitude());
                            node.put("dst_lat", location.getLatitude());
                        }
                    }

                    // send to kafka
                    Producer.getInstance().send(PropertyManager.getProperty("SENSOR_STREAM_OUTPUT_TOPIC"), node);
                });

                Producer.getInstance().close();
            });

            ((CanCommitOffsets) Consumer.getInstance().getStream(Spark.getStreamingContext()).inputDStream()).commitAsync(offsetRanges);
        });

        Spark.getStreamingContext().start();
        Spark.getStreamingContext().awaitTermination();
    }
}