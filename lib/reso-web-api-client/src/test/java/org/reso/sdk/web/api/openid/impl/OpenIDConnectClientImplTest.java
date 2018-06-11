package org.reso.sdk.web.api.openid.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;

public class OpenIDConnectClientImplTest {

	OpenIDConnectClient client = new OpenIDConnectClientBuilder()
			.setAuthorizeUrl("http://something/here")
			.setTokenUrl("http://something/here")
			.setClientId("someclientid")
			.setClientSecret("someclientsecret")
			.build();
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testGetAuthorizeUrlInvalidRedirectUrl() {
		thrown.expect(IllegalArgumentException.class);
		client.getAuthorizeURI("something here");
	}
	
	@Test
	public void testGetAuthorizeUrlNullScope() {
		thrown.expect(IllegalArgumentException.class);
		client.getAuthorizeURI("http://something/here", null);
	}
	
	@Test
	public void testGetAuthorizeUrlEmptyScope() {
		thrown.expect(IllegalArgumentException.class);
		client.getAuthorizeURI("http://something/here", " ");
	}
	
	@Test
	public void testAuthorizeNullUsername() {
		thrown.expect(IllegalArgumentException.class);
		client.authorize(null, "password", "http://something/here");
	}
	
	@Test
	public void testAuthorizeEmptyUsername() {
		thrown.expect(IllegalArgumentException.class);
		client.authorize(" ", "password", "http://something/here");
	}
	
	@Test
	public void testAuthorizeNullPassword() {
		thrown.expect(IllegalArgumentException.class);
		client.authorize("username", null, "http://something/here");
	}
	
	@Test
	public void testAuthorizeEmptyPassword() {
		thrown.expect(IllegalArgumentException.class);
		client.authorize("username", " ", "http://something/here");
	}
	
	@Test
	public void testAuthorizeInvalidRedirectUrl() {
		thrown.expect(IllegalArgumentException.class);
		client.authorize("username", " ", "something here");
	}
	
	@Test
	public void testGetTokensNullAuthCode() {
		thrown.expect(IllegalArgumentException.class);
		client.getTokens(null, "http://something/here");
	}
	
	@Test
	public void testGetTokensEmptyAuthCode() {
		thrown.expect(IllegalArgumentException.class);
		client.getTokens(" ", "http://something/here");
	}
	
	@Test
	public void testGetTokensInvalidRedirectUrl() {
		thrown.expect(IllegalArgumentException.class);
		client.getTokens("somecode", "something here");
	}
	
	@Test
	public void testRefreshTokensNullToken() {
		thrown.expect(IllegalArgumentException.class);
		client.refreshTokens(null);
	}
	
	@Test
	public void testRefreshTokensEmptyToken() {
		thrown.expect(IllegalArgumentException.class);
		client.refreshTokens(" ");
	}
}
