package org.reso.web.api.store.impl;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.web.api.openid.impl.TokenSetImpl;

public class DefaultTokenStoreTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testGetValidAccessTokenNotSet() {
		thrown.expect(IllegalStateException.class);
		DefaultTokenStore store = new DefaultTokenStore();
		store.getValidAccessToken();
	}
	
	@Test
	public void testGetValidAccessTokenInvalid() {
		thrown.expect(IllegalStateException.class);
		DefaultTokenStore store = new DefaultTokenStore();
		TokenSetImpl tokens = new TokenSetImpl();
		tokens.setAccessToken("some");
		tokens.setRefreshToken("some");
		tokens.setExpiresInSeconds(60);
		tokens.setTimeCreated(LocalDateTime.now().minusSeconds(65));
		store.storeTokens(tokens);
		store.getValidAccessToken();
	}
	
	@Test
	public void testGetValidAccessTokenValid() {
		DefaultTokenStore store = new DefaultTokenStore();
		TokenSetImpl tokens = new TokenSetImpl();
		tokens.setAccessToken("some");
		tokens.setRefreshToken("some");
		tokens.setExpiresInSeconds(60);
		tokens.setTimeCreated(LocalDateTime.now().minusSeconds(55));
		store.storeTokens(tokens);
		store.getValidAccessToken();
	}
	
}
