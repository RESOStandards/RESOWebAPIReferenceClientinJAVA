package org.reso.sdk.examples.web;

import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.client.impl.ResoClientBuilder;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;
import org.reso.sdk.web.api.store.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ClientExampleWebApplication {
	
	@Autowired
	Environment env;

	@Bean
	OpenIDConnectClient openIDConnectClient() {
		return OpenIDConnectClientBuilder.create()
				.setAuthorizeUrl(env.getProperty("authAuthorizeUrl"))
				.setTokenUrl(env.getProperty("authTokenUrl"))
				.setClientId(env.getProperty("clientId"))
				.setClientSecret(env.getProperty("clientSecret"))
				.build();
	}
	
	@Bean
	TokenStore tokenStore() {
		return new SessionTokenStore();
	}
	
	@Bean
	ResoClient resoClient(TokenStore tokenStore) {
		return ResoClientBuilder.create()
				.setBaseRequestUrl(env.getProperty("apiRequestUrl"))
				.setTokenStore(tokenStore)
				.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ClientExampleWebApplication.class, args);
	}
}
