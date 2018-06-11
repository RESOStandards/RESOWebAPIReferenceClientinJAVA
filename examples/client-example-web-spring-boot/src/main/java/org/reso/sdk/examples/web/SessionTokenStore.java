package org.reso.sdk.examples.web;

import javax.servlet.http.HttpSession;

import org.reso.sdk.web.api.openid.TokenSet;
import org.reso.sdk.web.api.store.TokenStore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionTokenStore implements TokenStore {
	
	private static final String TOKENS_ATTR = "tokens";

	@Override
	public void storeTokens(TokenSet tokenSet) {
	    getCurrentSession().setAttribute(TOKENS_ATTR, tokenSet);
	}

	@Override
	public TokenSet getStoredTokens() {
		return (TokenSet) getCurrentSession().getAttribute(TOKENS_ATTR); 
	}

	@Override
	public String getValidAccessToken() {
		TokenSet tokenSet = getStoredTokens();
		if (tokenSet == null)
			throw new RuntimeException("Tokens not stored in session");
		// TODO validate time
		return tokenSet.getAccessToken();
	}
	
	private HttpSession getCurrentSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true);
	}

}
