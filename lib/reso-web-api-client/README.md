# Reso Web API Java client library
Reso Web API Java client library allows developers to build applications by utilizing the Reso Web API. For more information please visit [www.reso.org](http://www.reso.org).

## Reso Web API as a dependency

### Local Installation

To build the client and install it to local repository run the following command:

```
mvn install
```

### Using jitpack.io

Jitpack builds and publishes git repositories on demand and provides ready to use artifacts. Your build tool will automatically download **reso-web-api-client** as a dependency without the need for local repository installation. Specifying a commit hash also ensures that all team members are using the same version.

####Maven snippet:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.RESOStandards.RESO-WebAPI-Client-Java</groupId>
    <artifactId>reso-web-api-client</artifactId>
    <version>[commit-hash]</version>
</dependency>
```
For more information, please see [jitpack.io](https://jitpack.io/#RESOStandards/RESO-WebAPI-Client-Java).

## Using the client
Here are some basic client usage steps to follow.

1. Build the instance of `OpenIDConnectClient` for authorization using `OpenIDConnectClientBuilder`.

```java
OpenIDConnectClient openIDClient = OpenIDConnectClientBuilder.create()
		.setAuthorizeUrl("https://example.org/authorize")
		.setTokenUrl("https://example.org/authorize")
		.setClientId("someClientId")
		.setClientSecret("someClientSecret")
		.build();
```

2. Create instance of `TokenStore` to store the `access` and `refresh` tokens received. There is a `DefaultTokenStore` provided here, which stores the token in a variable. Custom `TokenStore` implementation can be created by implementing the `TokenStore` interface if required (e.g. one could implement `SessionTokenStore` to store tokens in user session in WEB environment).

```java
TokeStore tokenStore = new DefaultTokenStore();
```

3. Authorize the user and receive the tokens.

```java
// authorize the user
String authCode = openIDClient.authorize("someuser", "somepassword", "https://example.org/some/redirect/url", "openid");

// get tokens and store them
tokenStore.storeTokens(openIDClient.getTokens(authCode, "https://example.org/some/redirect/url"));
```

4. Build the instance of `ResoClient` to execute requests with. Use `ResoClientBuilder` for that.

```java
ResoClient resoClient = ResoClientBuilder.create()
		.setBaseRequestUrl("https://example.org/odata/")
		.setTokenStore(tokenStore)
		.build();
```

5. Execute request against the Reso Web API using `ResoClient`.

```java
String jsonContent = resoClient.get("Property?$top=3", ResponseFormat.JSON);
System.out.println(jsonContent);
```

## More on OpenIDConnectClient
The `authorize` method of `OpenIDConnectClient` parses and fills the login form of the service provider, which may be different for each provider. The provided `DefaultLoginFormAdapter` expects the standard login page having `<form>` element with common input fields to enter `username` and `password`. `OpenIDConnectClientBuilder` can be used to provide custom `LoginFormAdapter` implementation.

```java
public class MyCustomLoginFormAdapter implements LoginFormAdapter {
	// ... implementation goes here
}

LoginFormAdapter customLoginFormAdapter = new MyCustomLoginFormAdapter();
builder.setLoginFormAdapter(customLoginFormAdapter);
```


## More on ResoClient
`ResoCient` can request data in `JSON` or `XML` formats, but always returns `String` value of the content. This is done to avoid tight coupling and allow developers to use variety of available libraries to deal with those formats. For example, one common use could be serializing response content to Java objects using `ObjectMapper`.

However, the library does provide two basic implementations in a form of wrappers. `JsonResoClientWrapper` serializes `JSON` response to `org.json.JSONObject`. `XmlResoClientWrapper` serializes `XML` response to `org.w3c.dom.Document`. Example usage of the wrapper is shown below.

```java
XmlResoClientWrapper xmlResoClient = new XmlResoClientWrapper(resoClient);
Document xml = xmlResoClient.getMetadata();
```
