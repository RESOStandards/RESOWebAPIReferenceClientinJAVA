package org.reso.sdk.web.api.openid.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.reso.sdk.web.api.openid.impl.TokenSetImpl;

public class TokenSetImplTest {

	@Test
	public void testNotValidIfExpired() {
		TokenSetImpl ts = new TokenSetImpl();
		ts.setAccessToken("accesstoken");
		ts.setRefreshToken("refreshtoken");
		ts.setTimeCreated(LocalDateTime.now().minusSeconds(65));
		ts.setExpiresInSeconds(60);
		assertFalse("Token still valid", ts.isValid());
	}
	
	@Test
	public void testValidIfNotExpired() {
		TokenSetImpl ts = new TokenSetImpl();
		ts.setAccessToken("accesstoken");
		ts.setRefreshToken("refreshtoken");
		ts.setTimeCreated(LocalDateTime.now().minusSeconds(55));
		ts.setExpiresInSeconds(60);
		assertTrue("Token not valid", ts.isValid());
	}
	
	@Test
	public void testNotValidIfEmptyAccessToken() {
		TokenSetImpl ts = new TokenSetImpl();
		ts.setRefreshToken("refreshtoken");
		ts.setTimeCreated(LocalDateTime.now());
		ts.setExpiresInSeconds(60);
		assertFalse("Token empty but is valid", ts.isValid());
		ts.setAccessToken("");
		assertFalse("Token empty but is valid", ts.isValid());
		ts.setAccessToken("  ");
		assertFalse("Token empty but is valid", ts.isValid());
	}
}
