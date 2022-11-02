package org.mataelang.kaspacore.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.mataelang.kaspacore.DataStream;
import org.mataelang.kaspacore.exceptions.KaspaCoreRuntimeException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Objects;

public class IPLookupTool {

    private static IPLookupTool instance;

    private final DatabaseReader reader;

    public IPLookupTool() {
        File maxmindDBFile = getFileFromResource(PropertyManager.getProperty("MAXMIND_DB_PATH"));
        try {
            this.reader = buildWithCache(maxmindDBFile);
        } catch (IOException e) {
            throw new KaspaCoreRuntimeException(e);
        }
    }

    private static File getFileFromResource(String filename) {
        URI maxmindDBFileUri;
        try {
            maxmindDBFileUri = Objects.requireNonNull(DataStream.class
                            .getClassLoader()
                            .getResource(filename))
                    .toURI();
        } catch (URISyntaxException e) {
            throw new KaspaCoreRuntimeException(e);
        }

        return new File(maxmindDBFileUri);
    }

    public static ObjectNode ipEnrichmentFunc(ConsumerRecord<String, JsonNode> messageNode) {
        ObjectNode objectNode = messageNode.value().deepCopy();

        if (objectNode.get("src_addr").isNull()) {
            return objectNode;
        }

        if (objectNode.get("dst_addr").isNull()) {
            return objectNode;
        }

        CountryResponse srcAddrCountry = getInstance().getCountry(objectNode.get("src_addr").textValue());
        CountryResponse dstAddrCountry = getInstance().getCountry(objectNode.get("dst_addr").textValue());

        if (srcAddrCountry != null) {
            objectNode.put("src_country_code", srcAddrCountry.getCountry().getIsoCode());
            objectNode.put("src_country_name", srcAddrCountry.getCountry().getName());
        }

        if (dstAddrCountry != null) {
            objectNode.put("dst_country_code", dstAddrCountry.getCountry().getIsoCode());
            objectNode.put("dst_country_name", dstAddrCountry.getCountry().getName());
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
            Logger.getLogger(DataStream.class).debug(e);
            return null;
        }

        return cityResponse;
    }

    public CountryResponse getCountry(String ipAddress) {
        InetAddress srcAddress;
        try {
            srcAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
            Logger.getLogger(this.getClass()).debug(e);
            return null;
        }

        CountryResponse countryResponse;
        try {
            countryResponse = IPLookupTool.getInstance().getReader().country(srcAddress);
        } catch (IOException e) {
            throw new KaspaCoreRuntimeException(e);
        } catch (GeoIp2Exception e) {
            Logger.getLogger(DataStream.class).debug(e);
            return null;
        }

        return countryResponse;
    }

    public DatabaseReader getReader() {
        return reader;
    }
}
