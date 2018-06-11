package org.reso.sdk.web.api.client.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.sdk.web.api.client.impl.ResoClientBuilder;
import org.reso.sdk.web.api.store.impl.DefaultTokenStore;

public class ResoClientBuilderTest {
	
	ResoClientBuilder builder = new ResoClientBuilder();
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testNullBaseRequestUrl() {
		thrown.expect(IllegalArgumentException.class);
		builder.setBaseRequestUrl("something here");
	}
	
	@Test
	public void testIncorrectBaseRequestUrl() {
		thrown.expect(IllegalArgumentException.class);
		builder.setBaseRequestUrl("something here");
	}
	
	@Test
	public void testCorrectBaseRequestUrl() {
		builder.setBaseRequestUrl("http://something:80/here");
	}
	
	@Test
	public void testNullTokenStore() {
		thrown.expect(IllegalArgumentException.class);
		builder.setTokenStore(null);
	}
	
	@Test
	public void testCorrectTokenStore() {
		builder.setTokenStore(new DefaultTokenStore());
	}
	
	@Test
	public void testBuildWithoutBaseRequestUrl() {
		thrown.expect(IllegalStateException.class);
		builder.setTokenStore(new DefaultTokenStore()).build();
	}
	
	@Test
	public void testBuildWithoutTokenStore() {
		thrown.expect(IllegalStateException.class);
		builder.setBaseRequestUrl("http://something/here").build();
	}
	
	@Test
	public void testBuildAllSet() {
		builder
			.setTokenStore(new DefaultTokenStore())
			.setBaseRequestUrl("http://something/here")
			.build();
	}

}
