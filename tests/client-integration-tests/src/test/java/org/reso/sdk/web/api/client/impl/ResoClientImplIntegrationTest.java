package org.reso.sdk.web.api.client.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.client.ResoClient.ResponseFormat;
import org.reso.sdk.web.api.client.impl.ResoClientBuilder;
import org.reso.sdk.web.api.client.wrapper.JsonResoClientWrapper;
import org.reso.sdk.web.api.client.wrapper.XmlResoClientWrapper;
import org.reso.sdk.web.api.exception.ApiCallException;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;
import org.reso.sdk.web.api.store.TokenStore;
import org.reso.sdk.web.api.store.impl.DefaultTokenStore;
import org.w3c.dom.Document;

public class ResoClientImplIntegrationTest {
	
	static ResoClient reso;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void beforeClass() {
		// load properties
		Properties properties = new Properties();
		try {
			properties.load(ResoClientImplIntegrationTest.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (Exception e) {
			fail("Could not load properties");
		}
		
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		String redirectUri = properties.getProperty("redirectUri");
		String scope = properties.getProperty("scope", "openid");
		String requestUrl = properties.getProperty("apiRequestUrl");
		
		// return if not configured
		if (username == null || username.isEmpty()) {
			System.out.println("----------");
			System.out.println("WARNING: ResoClientImplIntegrationTest not configured. Skipped!");
			System.out.println("----------");
			return;
		}
		
		OpenIDConnectClient openID = OpenIDConnectClientBuilder.create()
				.setAuthorizeUrl(properties.getProperty("authAuthorizeUrl"))
				.setTokenUrl(properties.getProperty("authTokenUrl"))
				.setClientId(properties.getProperty("clientId"))
				.setClientSecret(properties.getProperty("clientSecret"))
				.build();
		
		
		// create token store to store tokens
		TokenStore store = new DefaultTokenStore();
		
		// authorize the user
		String authCode = openID.authorize(username, password, redirectUri, scope);
		
		// get tokens and store them
		store.storeTokens(openID.getTokens(authCode, redirectUri));
		
		// create the Reso client
		reso = ResoClientBuilder.create()
				.setTokenStore(store)
				.setBaseRequestUrl(requestUrl)
				.build();
	}
	
	@Test
	public void testMetadataDefault() {
		if (reso == null) return;
		String response = reso.getMetadata(ResponseFormat.DEFAULT);
		assertNotNull("Response is null", response);
	}
	
	@Test
	public void testMetadataJson() {
		if (reso == null) return;
		String response = reso.getMetadata(ResponseFormat.JSON);
		assertNotNull("Response is null", response);
	}
	
	@Test
	public void testMetadataJsonWrapper() {
		if (reso == null) return;
		JsonResoClientWrapper jsonReso = new JsonResoClientWrapper(reso);
		JSONObject json = jsonReso.getMetadata();
		assertNotNull("JSON is null", json);
	}
	
	@Test
	public void testMetadataXml() {
		if (reso == null) return;
		String response = reso.getMetadata(ResponseFormat.XML);
		assertNotNull("Response is null", response);
	}
	
	@Test
	public void testMetadataXmlWrapper() {
		if (reso == null) return;
		XmlResoClientWrapper xmlReso = new XmlResoClientWrapper(reso);
		Document xml = xmlReso.getMetadata();
		assertNotNull("XML is null", xml);
	}
	
	
	@Test
	public void testSimpleRequest() {
		if (reso == null) return;
		String json = reso.get("Property?$top=3", ResponseFormat.JSON);
		assertNotNull("Response is null", json);
		assertTrue("json must not be empty", json.length() > 0);
		new JSONObject(json);
	}
	
	@Test
	public void testBadRequest() {
		if (reso == null) return;
		thrown.expect(ApiCallException.class);
		reso.get("Propertyyyy?$top=3", ResponseFormat.JSON);
	}
	
}
