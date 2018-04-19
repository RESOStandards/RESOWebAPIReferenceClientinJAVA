# Reso Web API Java client integration tests

This project is for integration testing against real API endpoints. 
It tests `OpenIDConnect` authorization and token retrieval and 
basic Reso Web API requests.

Fill the configuration properties in file:

```
src/main/resources/application.properties
```

Run the integration tests with the following command:

```
mvn test
```

Configure the logging level using `Logback` configuration file:

```
src/main/resources/logback.xml
```


