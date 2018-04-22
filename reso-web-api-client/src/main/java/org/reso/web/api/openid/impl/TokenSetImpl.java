package org.reso.web.api.openid.impl;

import java.time.LocalDateTime;

import org.reso.web.api.openid.TokenSet;

/**
 * Implementation of {@link TokenSet}.
 */
public class TokenSetImpl implements TokenSet {
	
	private String accessToken;
	private String refreshToken;
	private LocalDateTime timeCreated;
	private Integer expiresInSeconds;
	
	/**
	 * Gets access token.
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * Sets the access token.
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Gets refresh token.
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * Sets the refresh token.
	 * @param refreshToken
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	/**
	 * Gets the time this token set was created.
	 * @return
	 */
	public LocalDateTime getTimeCreated() {
		return timeCreated;
	}
	
	/**
	 * Sets the time this token set was created.
	 * @param timeCreated
	 */
	public void setTimeCreated(LocalDateTime timeCreated) {
		this.timeCreated = timeCreated;
	}
	
	/**
	 * Number in seconds access token expires in.
	 * @return
	 */
	public Integer getExpiresInSeconds() {
		return expiresInSeconds;
	}
	
	/**
	 * Sets the number in seconds access token expires in.
	 * @param expiresInSeconds
	 */
	public void setExpiresInSeconds(Integer expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}
	
	/**
	 * Tests if access token is still valid
	 * by comparing difference of now and create time 
	 * with the now expiration period in seconds.
	 */
	public boolean isValid() {
		if (accessToken == null || accessToken.trim().isEmpty()) return false;
		if (expiresInSeconds != null)
			return timeCreated.plusSeconds(expiresInSeconds).isAfter(LocalDateTime.now());
		return true;
	}
	
	@Override
	public String toString() {
		return "TokenSetImpl [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", timeCreated="
				+ timeCreated + ", expiresInSeconds=" + expiresInSeconds + "]";
	}
	
	

}
