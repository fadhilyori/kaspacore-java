package org.mataelang.kaspacore.utils;

import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.mataelang.kaspacore.exceptions.KaspaCoreRuntimeException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Functions {
    private Functions() {
    }

    public static StructType getSchemaFromFile() {
        URI uri;
        String content;

        try {
            uri = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("event_schema.json")).toURI();
            content = FileUtils.readFileToString(new File(uri), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new KaspaCoreRuntimeException(e);
        }

        return (StructType) DataType.fromJson(content);
    }
}
