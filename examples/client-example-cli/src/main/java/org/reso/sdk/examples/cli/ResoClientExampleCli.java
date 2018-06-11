package org.reso.sdk.examples.cli;

import java.io.FileInputStream;
import java.util.Properties;

import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.client.ResoClient.ResponseFormat;
import org.reso.sdk.web.api.client.impl.ResoClientBuilder;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.openid.impl.OpenIDConnectClientBuilder;
import org.reso.sdk.web.api.store.TokenStore;
import org.reso.sdk.web.api.store.impl.DefaultTokenStore;

public class ResoClientExampleCli {
	
	private Properties properties;
	private OpenIDConnectClient openIDClient;
	private ResoClient resoClient;
	private TokenStore tokenStore;
	private FileOutputResoClientWrapper resoWrapper;
	private String username;
	private String password;
	private String redirectUri;
	private String scope;
	
	public ResoClientExampleCli(String configFile) {
		// load properties
		try(FileInputStream propsStream = new FileInputStream(configFile)) {
			properties = new Properties();
			properties.load(propsStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// build OpenIDConnect client
		openIDClient = OpenIDConnectClientBuilder.create()
				.setAuthorizeUrl(properties.getProperty("authAuthorizeUrl"))
				.setTokenUrl(properties.getProperty("authTokenUrl"))
				.setClientId(properties.getProperty("clientId"))
				.setClientSecret(properties.getProperty("clientSecret"))
				.build();
		
		// build token store
		tokenStore = new DefaultTokenStore();
		
		// build Reso API client
		resoClient = ResoClientBuilder.create()
				.setBaseRequestUrl(properties.getProperty("apiRequestUrl"))
				.setTokenStore(tokenStore)
				.build();
		
		// set user and request details
		username = properties.getProperty("username");
		password = properties.getProperty("password");
		redirectUri = properties.getProperty("redirectUri");
		scope = properties.getProperty("scope", "openid");
		
		// create file output wrapper
		resoWrapper = new FileOutputResoClientWrapper(resoClient);
	}
	
	public String authorizeUser() {
		return openIDClient.authorize(username, password, redirectUri, scope);
	}
	
	public void loadTokens(String authCode) {
		tokenStore.storeTokens(openIDClient.getTokens(authCode, redirectUri));
	}
	
	public String executeRequest(String request) {
		return resoClient.get(request, ResponseFormat.JSON);
	}
	
	public void executeRequestToFile(String request, String fileName) {
		resoWrapper.get(request, ResponseFormat.JSON, fileName);
	}
	
	public static void main(String[] args) {
		String configFile = "application.properties";
		if (args.length > 0) {
			configFile = args[0];
			System.out.println("Using provided configuration properies file "+configFile);
		} else {
			System.out.println("Using default configuration properties file "+configFile);
		}
		
		ResoClientExampleCli cli = new ResoClientExampleCli(configFile);
		
		// authorize user
		String authCode = cli.authorizeUser();
		
		// load tokens
		cli.loadTokens(authCode);
		
		// execute request
		String content = cli.executeRequest("Property?$top=3");
		System.out.println("Request executed! Content length " + content.length());
		
		// execute request to file
		cli.executeRequestToFile("Property?$top=3", "output.json");
		System.out.println("Request executed! Written to file output.json");
	}
	
	

}
