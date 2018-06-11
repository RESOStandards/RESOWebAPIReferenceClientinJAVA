package org.reso.sdk.web.api.client.impl;

import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.store.TokenStore;
import org.reso.sdk.web.api.util.URLUtil;

/**
 * Builder to create {@link ResoClientImpl} instances.
 * It can be instantiated directly or using <code>static create</code> method.
 */
public class ResoClientBuilder {
	
	private String baseRequestUrl;
	private TokenStore tokenStore;
	
	/**
	 * Public constructor of the builder.
	 */
	public ResoClientBuilder() {}
	
	/**
	 * Static method to create the builder.
	 * @return New instance.
	 */
	public static ResoClientBuilder create() {
		return new ResoClientBuilder();
	}
	
	/**
	 * Validates and builds the {@link ResoClient} instance.
	 * This builder creates instances of {@link ResoClientImpl}.
	 */
	public ResoClient build() {
		if (baseRequestUrl == null) throw new IllegalStateException("baseRequestUrl is required to build ResoClient");
		if (tokenStore == null) throw new IllegalStateException("TokenStore is required to build ResoClient");
		
		return new ResoClientImpl(
				this.baseRequestUrl,
				this.tokenStore);
	}
	
	/**
	 * The base URL to perform requests against.
	 * This URL is prefixed to all requests that are executed using the client.
	 * @param requestUrl Full URL String, including scheme.
	 */
	public ResoClientBuilder setBaseRequestUrl(String requestUrl) {
		URLUtil.validateUrlParameter("BaseRequestUrl", requestUrl);
		this.baseRequestUrl = requestUrl;
		return this;
	}

	/**
	 * {@link TokenStore} to be used by the {@link ResoClientImpl} instance.
	 * @param store Instance of token store.
	 */
	public ResoClientBuilder setTokenStore(TokenStore store) {
		if (store == null) throw new IllegalArgumentException("TokenStore must not be null");
		this.tokenStore = store;
		return this;
	}

}
