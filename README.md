# DEPRECATION NOTICE
RESO is no longer maintaining or supporting this codebase.

If you are actively using this library and would like to "adopt" it, please contact dev@reso.org

This repository will be made private after August 1, 2021.


# RESO SDK JAVA

Reso Web API Java client library allows developers to build applications by utilizing the Reso Web API. For more information on the Real Estate Standards Organization (RESO) please visit [www.reso.org](http://www.reso.org) or contact [github@reso.org](mailto:github@reso.org). Developers wishing to better understand how to use and contribute to our standards visit [RESO Developers FAQ](https://www.reso.org/developer-faqs/working-with-github/).

Specific questions about this repository should be posted to the 'Issues' section with the [Issue Template](ISSUE_TEMPLATE.md). 

## Requirements

To build the client code and run the examples these components are required:

* Java JDK 8 or higher. Can be found [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* Apache Maven 3.x. Can be found [here](https://maven.apache.org/download.cgi)

You can verify component installation using commands:

```
java -version
mvn -version
```

## Installation

Before running the tests or examples You must build and install reso-web-api-client library to Your local maven repository. 
Navigate to the library project and execute `maven install` command:

```
cd lib/reso-web-api-client
mvn install
```

## Dependencies

All required dependencies will be downloaded by `maven` during the build.

## Getting Started

A short guide on how to start using the code set is provided in [here](./lib/reso-web-api-client/README.md).

## Example apps

Example apps are provided in the [examples](./examples) folder to demonstrate the usage of the SDK. 

* [client-example-cli](./examples/client-example-cli) - demonstrates the client usage in a simple CLI program.
* [client-example-web-spring-boot](./examples/client-example-web-spring-boot) - demonstrates the client usage in a WEB environment using `SpringBoot`.

## Specific usage guidelines

Running tests and examples requires valid authorization for the tested RESO endpoint. 
Before running tests and examples one must configure the connection properties that are usually located in `application.properties` files of each application.
Please follow the description of each application for the exact location of configuration file.

## Tests

Folder [tests](./tests) contains separate project for integration testing. Details on how to run integration tests are provided [here](./tests/client-integration-tests/README.md).

New integration tests may be created by adding them to the existing integration test project, or by creating a new integration test project in [tests](./tests) folder and providing the required project description.
Please follow the contributing guidelines when providing new tests.

## Contributing

Please read the [contributing guidelines](CONTRIBUTING.md) if You are interested in contributing to the project.
