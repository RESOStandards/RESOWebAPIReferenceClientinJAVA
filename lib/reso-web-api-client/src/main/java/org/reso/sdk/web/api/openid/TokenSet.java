package org.reso.sdk.web.api.openid;

/**
 * Holds access and refresh tokens.
 */
public interface TokenSet {
	
	/**
	 * Return the access token.
	 * @return access token string value.
	 */
	String getAccessToken();
	
	/**
	 * Returns the refresh token.
	 * @return refresh token string value.
	 */
	String getRefreshToken();
	
	/**
	 * Returns <code>true</code> if access token is still valid.
	 * @return <code>true</code> if valid.
	 */
	boolean isValid();
}
