# Reso Web API Java client
This is a parent project for Reso Web API Java client that allows developers to build applications by utilizing the Reso Web API. For more information please visit [www.reso.org](http://www.reso.org).

It contains following sub projects:
* [reso-web-api-client](./reso-web-api-client) - the main project for the Reso Web API Java client containing the implemetation.
* [client-integration-tests](./client-integration-tests) - project for the integration tests against real service endpoints.
* [client-example-cli](./client-example-cli) - demonstrates the client usage in simple CLI program.
* [client-example-web-spring-boot](./client-example-web-spring-boot) - demonstrates the client usage in a WEB environment using `SpringBoot`.

Please follow the links for more detailed description of each project.

## Requirements
To build the client code and run the examples these components are required:
* Java 1.8 JDK to be installed. Can be found [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Apache Maven to be installed. Can be found [here](https://maven.apache.org).

You can verify the installation using commands:

```
java -version
mvn -version
```

## Running examples
To run the examples one needs to build the client library and install it to local maven repository. 
This can be done by running the command:

```
mvn install
```

**Note.** Integration tests are skipped until they are configured for specific endpoint.