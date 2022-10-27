package org.mataelang.kaspacore;


import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.mataelang.kaspacore.providers.Consumer;
import org.mataelang.kaspacore.providers.Spark;
import org.mataelang.kaspacore.utils.IPLookupTool;
import org.mataelang.kaspacore.utils.PropertyManager;

public class DataStream {
    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger(DataStream.class).setLevel(
                Level.toLevel(
                        PropertyManager.getInstance().getProperty("LOG_LEVEL"),
                        Level.DEBUG
                )
        );

        Consumer.getInstance().getStream(Spark.getStreamingContext()).foreachRDD(rdd -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

            rdd.foreachPartition(recordIterator -> {
//                Producer.getInstance().connect();

//                OffsetRange o = offsetRanges[TaskContext.get().partitionId()];

                // send to kafka
                recordIterator.forEachRemaining(message -> {
                    // TODO: lookup ip addr with maxmind geoip
                    JsonNode srcAddrNode = message.value().get("src_addr");

                    if (srcAddrNode != null) {
                        String srcAddr = srcAddrNode.textValue();

                        CityResponse srcAddrGeoIPDetail = IPLookupTool.getInstance().get(srcAddr);

                        if (srcAddrGeoIPDetail != null) {
                            Country country = srcAddrGeoIPDetail.getCountry();
                            City city = srcAddrGeoIPDetail.getCity();
                            Logger.getLogger(DataStream.class).debug("IPAddress=" + srcAddr
                                    + " CountryISO=\"" + country.getIsoCode() + "\""
                                    + " CountryName=\"" + country.getName() + "\""
                                    + " City=\"" + city.getName() + "\""
                            );
                        }
                    }

                    // TODO: add the value with src_country_code key into the record

                    // send to kafka
//                    Producer.getInstance().send(result);
                });

//                Producer.getInstance().close();
            });

//            ((CanCommitOffsets) Consumer.getInstance().getStream(Spark.getStreamingContext()).inputDStream()).commitAsync(offsetRanges);
        });

        Spark.getStreamingContext().start();
        Spark.getStreamingContext().awaitTermination();
    }
}