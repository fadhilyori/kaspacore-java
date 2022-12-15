# Kaspacore Java

## Requirements
 - Spark min 3.3.1
 - Scala 2.13
 - openJDK min 11
 - Maven

## How to build
 - `mvn compile`
 - `mvn package`
   
The `.jar` file will be available at `target` directory, you can use `kaspacore-1.1-SNAPSHOT-jar-with-dependencies.
jar` to deploy it to Spark.

## Submit the App to Spark

You can submit App using `spark-submit` command.