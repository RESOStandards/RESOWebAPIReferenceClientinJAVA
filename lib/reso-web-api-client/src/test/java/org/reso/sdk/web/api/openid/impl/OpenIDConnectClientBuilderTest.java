package org.reso.sdk.web.api.openid.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;

public class OpenIDConnectClientBuilderTest {

	OpenIDConnectClientBuilder builder = new OpenIDConnectClientBuilder();
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testNotValidAuthorizeUrl() {
		thrown.expect(IllegalArgumentException.class);
		builder.setAuthorizeUrl("something here");
	}
	
	@Test
	public void testValidAuthorizeUrl() {
		builder.setAuthorizeUrl("https://something/here");
	}
	
	@Test
	public void testNotValidTokenUrl() {
		thrown.expect(IllegalArgumentException.class);
		builder.setTokenUrl("something here");
	}
	
	@Test
	public void testValidTokenUrl() {
		builder.setTokenUrl("https://something/here");
	}
	
	@Test
	public void testNullClientId() {
		thrown.expect(IllegalArgumentException.class);
		builder.setClientId(null);
	}
	
	@Test
	public void testNotValidClientId() {
		thrown.expect(IllegalArgumentException.class);
		builder.setClientId(" ");
	}
	
	@Test
	public void testValidClientId() {
		builder.setClientId("someid");
	}
	
	@Test
	public void testNullClientSecret() {
		thrown.expect(IllegalArgumentException.class);
		builder.setClientSecret(null);
	}
	
	@Test
	public void testNotValidClientSecret() {
		thrown.expect(IllegalArgumentException.class);
		builder.setClientSecret(" ");
	}
	
	@Test
	public void testValidClientSecret() {
		builder.setClientSecret("somesecret");
	}
	
	@Test
	public void testNullloginFormAdapter() {
		thrown.expect(IllegalArgumentException.class);
		builder.setLoginFormAdapter(null);
	}
	
	@Test
	public void testBuildNullAuthorizeUrl() {
		thrown.expect(IllegalStateException.class);
		builder
			.setTokenUrl("http://something/here")
			.setClientId("someclientid")
			.setClientSecret("someclientsecret")
			.build();
	}
	
	@Test
	public void testBuildNullTokenUrl() {
		thrown.expect(IllegalStateException.class);
		builder
			.setAuthorizeUrl("http://something/here")
			.setClientId("someclientid")
			.setClientSecret("someclientsecret")
			.build();
	}
	
	@Test
	public void testBuildNullClientId() {
		thrown.expect(IllegalStateException.class);
		builder
			.setAuthorizeUrl("http://something/here")
			.setTokenUrl("http://something/here")
			.setClientSecret("someclientsecret")
			.build();
	}
	
	@Test
	public void testBuildNullClientSecret() {
		thrown.expect(IllegalStateException.class);
		builder
			.setAuthorizeUrl("http://something/here")
			.setTokenUrl("http://something/here")
			.setClientId("someclientid")
			.build();
	}
	
	@Test
	public void testBuildNullLoginFormAdapter() {
		builder
			.setAuthorizeUrl("http://something/here")
			.setTokenUrl("http://something/here")
			.setClientId("someclientid")
			.setClientSecret("someclientsecret")
			.build();
	}
}
