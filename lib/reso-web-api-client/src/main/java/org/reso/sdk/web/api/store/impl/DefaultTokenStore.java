package org.reso.sdk.web.api.store.impl;

import org.reso.sdk.web.api.openid.TokenSet;
import org.reso.sdk.web.api.store.TokenStore;

/**
 * Default implementation of {@link TokenStore}.
 * Stores tokens in a variable.
 */
public class DefaultTokenStore implements TokenStore {

	private TokenSet tokenSet;

	@Override
	public void storeTokens(TokenSet tokenSet) {
		this.tokenSet = tokenSet;
	}

	@Override
	public TokenSet getStoredTokens() {
		return this.tokenSet;
	}

	@Override
	public String getValidAccessToken() {
		if (this.tokenSet == null) throw new IllegalStateException("Tokens must be set before requesting valid access token");
		if (!this.tokenSet.isValid()) throw new IllegalStateException("Tokens are not valid");
		return this.tokenSet.getAccessToken();
	}
	
}
