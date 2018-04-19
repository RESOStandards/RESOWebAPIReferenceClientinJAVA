# Reso Web API Java client CLI example

This example is a simple Reso Web API CLI implementation. 
It uses configuration properties to setup the `ResoClient` and execute simple request. 

Fill the configuration properties in file:

```
application.properties
```

Build the client using maven command:

```
mvn package
```

Run the CLI using:

```
java -jar target/client-example-cli-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Configure the logging level using Logback configuration file:

```
src/main/resources/logback.xml
```