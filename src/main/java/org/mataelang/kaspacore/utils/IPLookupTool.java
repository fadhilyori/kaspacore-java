package org.mataelang.kaspacore.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
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

    public static ObjectNode ipEnrichmentFunc(ObjectNode messageNode, JsonNode addrNode, String prefix) {
        if (addrNode != null) {
            String addr = addrNode.textValue();
            CityResponse addrGeoIPDetail = getInstance().get(addr);

            if (addrGeoIPDetail != null) {
                Country country = addrGeoIPDetail.getCountry();
                City city = addrGeoIPDetail.getCity();
                Location location = addrGeoIPDetail.getLocation();
                messageNode.put(prefix + "_country_code", country.getIsoCode());
                messageNode.put(prefix + "_country_name", country.getName());
                if (city.getName() != null) {
                    messageNode.put(prefix + "_city_name", city.getName());
                }
                messageNode.put(prefix + "_long", location.getLongitude());
                messageNode.put(prefix + "_lat", location.getLatitude());
            }
        }
        return messageNode;
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

    public CityResponse get(String ipAddress) {
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

    public DatabaseReader getReader() {
        return reader;
    }
}
