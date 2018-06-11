package org.reso.sdk.web.api.openid.impl;

import org.reso.sdk.web.api.openid.LoginFormAdapter;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.util.URLUtil;

/**
 * Builder for the {@link OpenIDConnectClient}.
 * This class is intended to use for client creation.
 * Creates new instance of {@link OpenIDConnectClientImpl}. 
 */
public class OpenIDConnectClientBuilder {
	
	private String authorizeUrl;
	private String tokenUrl;
	private String clientId;
	private String clientSecret;
	private LoginFormAdapter loginFormAdapter;
	
	/**
	 * Static method to create instance of this builder.
	 * @return Instance of this.
	 */
	public static OpenIDConnectClientBuilder create() {
		return new OpenIDConnectClientBuilder();
	}
	
	/**
	 * Validates the provided parameters and builds 
	 * the instance of {@link OpenIDConnectClient}.
	 * @return
	 */
	public OpenIDConnectClient build() {
		if (authorizeUrl == null) throw new IllegalStateException("authorizeUrl required but not set");
		if (tokenUrl == null) throw new IllegalStateException("tokenUrl required but not set");
		if (clientId == null) throw new IllegalStateException("clientId required but not set");
		if (clientSecret == null) throw new IllegalStateException("clientSecret required but not set");
		
		// create default loginFormAdapter
		// if one is not set
		if (loginFormAdapter == null) {
			loginFormAdapter = new DefaultLoginFormAdapter(authorizeUrl);
		}
		
		return new OpenIDConnectClientImpl(
				this.authorizeUrl,
				this.tokenUrl,
				this.clientId,
				this.clientSecret,
				this.loginFormAdapter);
	}
	
	/**
	 * Full authorize end point URL.
	 * @param authorizeUrl Full authorize end point URL.
	 * @return
	 */
	public OpenIDConnectClientBuilder setAuthorizeUrl(String authorizeUrl) {
		URLUtil.validateUrlParameter("AuthorizeUrl", authorizeUrl);
		this.authorizeUrl = authorizeUrl;
		return this;
	}

	/**
	 * Validates and sets the full token end point URL.
	 * @param tokenUrl Full token end point URL.
	 * @return
	 */
	public OpenIDConnectClientBuilder setTokenUrl(String tokenUrl) {
		URLUtil.validateUrlParameter("AuthorizeUrl", tokenUrl);
		this.tokenUrl = tokenUrl;
		return this;
	}
	
	/**
	 * Validates and sets the Id of the client.
	 * @param clientId ID of the client.
	 * @return
	 */
	public OpenIDConnectClientBuilder setClientId(String clientId) {
		if (clientId == null || clientId.trim().isEmpty()) 
			throw new IllegalArgumentException("ClientID must not be null or empty");
		this.clientId = clientId.trim();
		return this;
	}

	/**
	 * Validates and sets the secret of the client.
	 * @param clientSecret Secret of the client.
	 * @return
	 */
	public OpenIDConnectClientBuilder setClientSecret(String clientSecret) {
		if (clientSecret == null || clientSecret.trim().isEmpty()) 
			throw new IllegalArgumentException("ClientSecret must not be null or empty");
		this.clientSecret = clientSecret.trim();
		return this;
	}

	/**
	 * Sets the {@link LoginFormAdapter} implementation for login form
	 * parsing and filling. If not provided, {@link DefaultLoginFormAdapter}
	 * will be created for the client implementation.
	 * @param loginFormAdapter Implementation of adapter.
	 */
	public void setLoginFormAdapter(LoginFormAdapter loginFormAdapter) {
		if (loginFormAdapter == null) throw new IllegalArgumentException("LoginFormAdapter must not be null");
		this.loginFormAdapter = loginFormAdapter;
	}

}
