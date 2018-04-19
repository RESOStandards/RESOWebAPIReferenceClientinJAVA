# Reso Web API Java client Spring Boot example

This example demonstrates the `ResoClient` usage in WEB environment.
Example makes use of `SpringBoot` framework to create a simple WEB application.
It uses `Thymeleaf` and `Bootstrap 4.0` to render web pages.

Example supports the following use cases:
* User login with custom login page.
* Token storage in user session using `SessionTokenStore` implementation.
* API request execution.

**Note.** The code used in the example is not ready for production. The simple login page implementation does not address the required security concerns such as CSRF protection and etc.

Fill the configuration properties in file:

```
src/main/resources/application.properties
```

Run the client using maven command:

```
mvn spring-boot:run
```

This should start the server at port `8080` on the local machine.

Configure the logging level using `Logback` configuration file:

```
src/main/resources/logback.xml
```