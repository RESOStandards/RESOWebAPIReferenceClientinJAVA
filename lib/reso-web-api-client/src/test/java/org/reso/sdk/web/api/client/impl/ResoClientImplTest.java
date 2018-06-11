package org.reso.sdk.web.api.client.impl;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.sdk.web.api.client.ResoClient.ResponseFormat;
import org.reso.sdk.web.api.client.impl.ResoClientBuilder;
import org.reso.sdk.web.api.client.impl.ResoClientImpl;
import org.reso.sdk.web.api.openid.impl.TokenSetImpl;
import org.reso.sdk.web.api.store.TokenStore;
import org.reso.sdk.web.api.store.impl.DefaultTokenStore;

public class ResoClientImplTest {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	ResoClientImpl client;
	
	@Before
	public void before() {
		TokenSetImpl tokens = new TokenSetImpl();
		tokens.setAccessToken("some");
		tokens.setRefreshToken("some");
		tokens.setExpiresInSeconds(60);
		tokens.setTimeCreated(LocalDateTime.now());
		
		TokenStore store = new DefaultTokenStore();
		store.storeTokens(tokens);
		
		client = (ResoClientImpl) ResoClientBuilder.create()
				.setBaseRequestUrl("https://something/here")
				.setTokenStore(store)
				.build();
	}
	

	@Test
	public void testContructorInvalidUrl() {
		thrown.expect(IllegalArgumentException.class); 
		new ResoClientImpl("something here", new DefaultTokenStore());
	}
	
	@Test
	public void testContructorNullTokenStore() {
		thrown.expect(IllegalArgumentException.class); 
		new ResoClientImpl("https://something/here", null);
	}
	
	@Test
	public void testGetNullRequest() {
		thrown.expect(IllegalArgumentException.class); 
		client.get(null, ResponseFormat.JSON);
	}
	
	@Test
	public void testGetEmptyRequest() {
		thrown.expect(IllegalArgumentException.class); 
		client.get(" ", ResponseFormat.JSON);
	}
	
	@Test
	public void testBuildFinalURI() {
		// complex uri
		URI uri = client.buildFinalURI("Property?$top=4&$select=ListingKey,BedroomsTotal,OwnerName,ListingContractDate,ModificationTimestamp,StandardStatus,PropertyType,Location&$filter=BedroomsTotal eq 2 and PropertyType eq 'Residential'");
		assertEquals("Wrong URI value", "https://something/here/Property?%24top=4&%24select=ListingKey%2CBedroomsTotal%2COwnerName%2CListingContractDate%2CModificationTimestamp%2CStandardStatus%2CPropertyType%2CLocation&%24filter=BedroomsTotal+eq+2+and+PropertyType+eq+%27Residential%27", uri.toString());
		
		// no query
		uri = client.buildFinalURI("Property");
		assertEquals("Wrong URI value", "https://something/here/Property", uri.toString());
		
		// with /
		uri = client.buildFinalURI("/Property");
		assertEquals("Wrong URI value", "https://something/here/Property", uri.toString());
	}
	
}
