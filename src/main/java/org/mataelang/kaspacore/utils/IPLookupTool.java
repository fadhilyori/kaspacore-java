package org.mataelang.kaspacore.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.log4j.Logger;
import org.mataelang.kaspacore.DataStream;

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
        File maxmindDBFile = getFileFromResource(PropertyManager.getInstance().getProperty("maxmindDatabaseFilePath"));
        try {
            this.reader = buildWithCache(maxmindDBFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

        return new File(maxmindDBFileUri);
    }

    private static DatabaseReader build(File maxmindDBFile) throws IOException {
        return new DatabaseReader.Builder(maxmindDBFile).build();
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
            throw new RuntimeException(e);
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
