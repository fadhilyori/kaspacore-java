package org.mataelang.kaspacore.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import io.netty.util.internal.ResourcesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.SparkFiles;
import org.apache.spark.resource.ResourceUtils;
import org.mataelang.kaspacore.exceptions.KaspaCoreRuntimeException;
import org.mataelang.kaspacore.jobs.SensorEnrichDataStreamJob;
import org.mataelang.kaspacore.providers.Spark;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Objects;

public class IPLookupTool {

    private static IPLookupTool instance;

    private final DatabaseReader reader;

    public IPLookupTool() {
        File maxmindDBFile = new File(SparkFiles.get(PropertyManager.getProperty("MAXMIND_DB_FILENAME")));
        try {
            this.reader = buildWithCache(maxmindDBFile);
        } catch (IOException e) {
            throw new KaspaCoreRuntimeException(e);
        }
    }

    public static ObjectNode ipEnrichmentFunc(ConsumerRecord<String, JsonNode> messageNode) {
        ObjectNode objectNode = messageNode.value().deepCopy();

        if (!objectNode.has("src_addr")) {
            return objectNode;
        }

        if (!objectNode.has("dst_addr")) {
            return objectNode;
        }

        CityResponse srcAddrCountry = getInstance().getCountry(objectNode.get("src_addr").textValue());
        CityResponse dstAddrCountry = getInstance().getCountry(objectNode.get("dst_addr").textValue());
        ObjectMapper mapper = new ObjectMapper();

        if (srcAddrCountry != null) {
            ObjectNode srcLocation = mapper.createObjectNode();

            srcLocation.put("lat", srcAddrCountry.getLocation().getLatitude());
            srcLocation.put("lon", srcAddrCountry.getLocation().getLongitude());

            objectNode.put("src_country_code", srcAddrCountry.getCountry().getIsoCode());
            objectNode.put("src_country_name", srcAddrCountry.getCountry().getName());
            objectNode.set("src_location", srcLocation);
        }

        if (dstAddrCountry != null) {
            ObjectNode dstLocation = mapper.createObjectNode();

            dstLocation.put("lat", dstAddrCountry.getLocation().getLatitude());
            dstLocation.put("lon", dstAddrCountry.getLocation().getLongitude());

            objectNode.put("dst_country_code", dstAddrCountry.getCountry().getIsoCode());
            objectNode.put("dst_country_name", dstAddrCountry.getCountry().getName());
            objectNode.set("dst_location", dstLocation);
        }

        return objectNode;
    }

    private static DatabaseReader buildWithCache(File maxmindDBFile) throws IOException {
        return new DatabaseReader.Builder(maxmindDBFile).withCache(new CHMCache()).build();
    }

    public static IPLookupTool getInstance() {
        if (instance == null) {
            instance = new IPLookupTool();
        }
        return instance;
    }

    public CityResponse getCity(String ipAddress) {
        InetAddress srcAddress;
        try {
            srcAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            Logger.getLogger(this.getClass()).debug(e);
            return null;
        }

        CityResponse cityResponse;
        try {
            cityResponse = IPLookupTool.getInstance().getReader().city(srcAddress);
        } catch (IOException e) {
            throw new KaspaCoreRuntimeException(e);
        } catch (GeoIp2Exception e) {
            Logger.getLogger(SensorEnrichDataStreamJob.class).debug(e);
            return null;
        }

        return cityResponse;
    }

    public CityResponse getCountry(String ipAddress) {
        InetAddress srcAddress;
        try {
            srcAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            Logger.getLogger(this.getClass()).debug(e);
            return null;
        }

        CityResponse countryResponse;
        try {
            countryResponse = IPLookupTool.getInstance().getReader().city(srcAddress);
        } catch (IOException e) {
            throw new KaspaCoreRuntimeException(e);
        } catch (GeoIp2Exception e) {
            Logger.getLogger(SensorEnrichDataStreamJob.class).debug(e);
            return null;
        }

        return countryResponse;
    }

    public DatabaseReader getReader() {
        return reader;
    }
}
