package org.reso.sdk.web.api.openid.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.openid.TokenSet;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;

public class OpenIDConnectClientIntegrationTest {
	
	Properties properties;
	
	@Before
	public void before() throws Exception {
		// load properties
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
		} catch (Exception e) {
			fail("Could not load properties");
		}
	}
	
	@Test
	public void test() {
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		String redirectUri = properties.getProperty("redirectUri");
		String scope = properties.getProperty("scope");
		
		// return if not configured
		if (username == null || username.isEmpty()) {
			System.out.println("----------");
			System.out.println("WARNING: OpenIDConnectClientIntegrationTest not configured. Skipped!");
			System.out.println("----------");
			return;
		}
		
		OpenIDConnectClient client = OpenIDConnectClientBuilder.create()
				.setAuthorizeUrl(properties.getProperty("authAuthorizeUrl"))
				.setTokenUrl(properties.getProperty("authTokenUrl"))
				.setClientId(properties.getProperty("clientId"))
				.setClientSecret(properties.getProperty("clientSecret"))
				.build();
		
		// authorize URI
		URI authURI = client.getAuthorizeURI(redirectUri, scope);
		assertNotNull("Authorization URI missing", authURI);
		
		// authorize
		String authCode = client.authorize(username, password, redirectUri, scope);
		assertNotNull("Authorization code missing", authCode);
		
		// get access token
		TokenSet tokens = client.getTokens(authCode, redirectUri);
		assertNotNull("Tokens missing", tokens);
		
		// refresh tokens
		tokens = client.refreshTokens(tokens.getRefreshToken());
		assertNotNull("Tokens missing", tokens);
	}

}
