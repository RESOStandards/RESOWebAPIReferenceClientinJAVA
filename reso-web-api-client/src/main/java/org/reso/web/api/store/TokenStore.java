package org.reso.web.api.store;

import org.reso.web.api.openid.TokenSet;

/**
 * Abstraction to store tokens used by the client.
 */
public interface TokenStore {

	/**
	 * Stores {@link TokenSet} provided.
	 * @param tokenSet Tokens to store.
	 */
	void storeTokens(TokenSet tokenSet);
	
	/**
	 * Gets the stored {@link TokenSet} as is
	 * without any validation.
	 * @return Stored tokens or null.
	 */
	TokenSet getStoredTokens();
	
	/**
	 * Gets the valid access token from stored tokens.
	 * This should validate if access token is set 
	 * and is still valid.
	 * @return Valid access token of throws exception.
	 */
	String getValidAccessToken();
	
}
