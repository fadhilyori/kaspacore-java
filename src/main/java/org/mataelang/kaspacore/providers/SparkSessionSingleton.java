package org.mataelang.kaspacore.providers;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkSessionSingleton {
    private static SparkSession instance;

    public static SparkSession getInstance(SparkConf sparkConf) {
        if (instance == null) {
            instance = SparkSession.builder().config(sparkConf).getOrCreate();
        }

        return instance;
    }
}
