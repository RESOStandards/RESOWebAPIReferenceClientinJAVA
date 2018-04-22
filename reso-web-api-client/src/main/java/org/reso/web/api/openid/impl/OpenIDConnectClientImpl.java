package org.reso.web.api.openid.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.reso.web.api.client.impl.ResoClientImpl;
import org.reso.web.api.exception.ApiCallException;
import org.reso.web.api.openid.LoginCommand;
import org.reso.web.api.openid.LoginFormAdapter;
import org.reso.web.api.openid.OpenIDConnectClient;
import org.reso.web.api.openid.TokenSet;
import org.reso.web.api.util.URLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link OpenIDConnectClient}.
 * This class is not intended to be instantiated directly.
 * User {@link OpenIDConnectClientBuilder} instead. 
 */
public class OpenIDConnectClientImpl implements OpenIDConnectClient {
	
	private static final Logger log = LoggerFactory.getLogger(ResoClientImpl.class);
	
	private final String authorizeUrl;
	private final String tokenUrl;
	private final String clientId;
	private final String clientSecret;
	private final LoginFormAdapter loginFormAdapter;
	
	/**
	 * Default scope of the service.
	 */
	public static final String DEFAULT_SCOPE = "ODataApi";
	
	/**
	 * Default access scope constructor
	 * intended to be used by the {@link OpenIDConnectClientBuilder}.
	 */
	OpenIDConnectClientImpl(
			final String authorizeUrl,
			final String tokenUrl,
			final String clientId,
			final String clientSecret,
			final LoginFormAdapter loginFormAdapter) {
		this.authorizeUrl = authorizeUrl;
		this.tokenUrl = tokenUrl;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.loginFormAdapter = loginFormAdapter;
	}
	
	//------------------------------------
	// getAuthorizeURI
	//------------------------------------
	
	@Override
	public URI getAuthorizeURI(String redirectUri) {
		return getAuthorizeURI(redirectUri, DEFAULT_SCOPE);
	}
	
	@Override
	public URI getAuthorizeURI(String redirectUri, String scope) {
		if (log.isDebugEnabled())
			log.debug("Getting authorize URI for client "+clientId+" and scope "+scope+" and redirect to "+redirectUri);
		
		// validate
		URLUtil.validateUrlParameter("RedirectURI", redirectUri);
		if (scope == null || scope.trim().isEmpty()) 
			throw new IllegalArgumentException("scope must not be null or empty");
		
		// trim values
		scope = scope.trim();
		
		try {
			URI uri = new URIBuilder(authorizeUrl)
					.addParameter("client_id", clientId)
					.addParameter("scope", scope)
					.addParameter("response_type", "code")
					.addParameter("redirect_uri", redirectUri)
					.build();
			if (log.isInfoEnabled()) log.info("Returning authorize URI: "+uri.toString());
			return uri;
		} catch (URISyntaxException e) {
			log.error("Could not build authorize URI: "+e.getMessage(), e);
			throw new IllegalArgumentException("Unable to build authorize URI: "+e.getMessage(), e);
		}
	}
	
	//------------------------------------
	// authorize
	//------------------------------------
	
	@Override
	public String authorize(String username, String password, String redirectUri) {
		return authorize(username, password, redirectUri, DEFAULT_SCOPE);
	}
	
	@Override
	public String authorize(String username, String password, String redirectUri, String scope) {
		if (log.isInfoEnabled())
			log.info("Authorizing user "+username+" for client "+clientId+" and scope "+scope+" and redirect to "+redirectUri);
		
		// validation
		if (username == null || username.trim().isEmpty()) 
			throw new IllegalArgumentException("username must not be null or empty");
		if (password == null || password.trim().isEmpty()) 
			throw new IllegalArgumentException("password must not be null or empty");
		
		// trim values
		username = username.trim();
		password = password.trim();
		
		// try building the URI
		URI uri = getAuthorizeURI(redirectUri, scope);
		
		// create the closable client to work with
		try (CloseableHttpClient client = getHttpClientBuilder().build()) {
			
			// create client context to store
			// the cookies for the requests
			HttpClientContext context = HttpClientContext.create();
			CookieStore cookieStore = new BasicCookieStore();
			context.setCookieStore(cookieStore);
			
			// build authorization URL
			// and create a HTTP GET request
            HttpGet httpGet = new HttpGet(uri);
            
            // Execute the request
            CloseableHttpResponse authResponse = client.execute(httpGet, context);
            String responseBody = EntityUtils.toString(authResponse.getEntity());
            
            if (log.isDebugEnabled())
            	log.debug("Authorize response: " + authResponse);
            
            // check if response is successful
            int authRespStatus = authResponse.getStatusLine().getStatusCode();
            if (authRespStatus < 200 || authRespStatus >= 300) {
            	throw new ApiCallException(authRespStatus, authResponse.getStatusLine().toString(), responseBody);
            }
            
            if (log.isDebugEnabled())
            	log.debug("Authorize login page content: "+responseBody);
            
            // parse and fill the login form 
            // to submit it using the POST request
            LoginCommand loginParams = loginFormAdapter.parseAndFill(responseBody, username, password);
            
            // Create HTTP POST request to login the user
            // using form submit
			HttpPost post = new HttpPost(loginParams.getActionUrl());
			post.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded"));
			
			// create post parameters by adding
			// all login form params as post entity
			List<NameValuePair> params = loginParams.getInputValues()
					.entrySet()
					.stream()
					.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
			post.setEntity(new UrlEncodedFormEntity(params));
			
			if (log.isInfoEnabled())
            	log.info("Posting user credentials to login action URL: "+loginParams.getActionUrl());
			
			// execute the login POST request
			// We do not inspect the response 
			// because we only need a last redirect URL
			// to retrieve the code parameter
			CloseableHttpResponse postResponse = client.execute(post, context);
			
			if (log.isDebugEnabled()) {
				log.debug("Login post headers: "+Arrays.asList(context.getRequest().getAllHeaders()).stream().map(h -> h.toString()).collect(Collectors.joining(", ")));
				log.debug("Login post parameters: "+params);
            	log.debug("Login post response: " + postResponse);
            	log.debug("Login post response content: " + EntityUtils.toString(postResponse.getEntity()));
			}
			
			// We need to get the authorization code
			// from the last redirect URL of successful post
			// so we find the last redirect here
        	URI finalUrl = post.getURI();
        	List<URI> locations = context.getRedirectLocations();
        	if (locations != null && locations.size() > 0) {
        	    finalUrl = locations.get(locations.size() - 1);
        	}
        	
        	if (log.isDebugEnabled()) {
        		log.debug("Redirect locations: "+locations);
        		log.debug("Resolved final redirect URL to: "+finalUrl);
        	}
        		
        	
        	// now we inspect the last redirect URL
        	// to find the code parameter in it
        	String code = null;
        	List<NameValuePair> finalParams = URLEncodedUtils.parse(finalUrl, Charset.forName("UTF-8"));
        	for (NameValuePair param : finalParams) {
        		if ("code".equals(param.getName())) {
        			code = param.getValue();
        			break;
        		}
        	}
        	if (log.isDebugEnabled())
        		log.debug("Resolved authorization code to: "+code);
        	
        	// throw on null code
        	if (code == null) 
        		throw new RuntimeException("Could not retrieve the authorization code while performing authorization");
            
            return code;
        } catch (ApiCallException ace) {
        	log.error("API call exception: "+ace.getMessage(), ace);
        	if (log.isDebugEnabled()) log.debug("Response content: "+ace.getResponseContent());
			throw ace;
		} catch (Exception e) {
			log.error("Request exception: "+e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	//------------------------------------
	// tokens
	//------------------------------------
	
	public TokenSet getTokens(String authCode, String redirectUri) {
		if (log.isInfoEnabled())
			log.info("Requesting tokens for client "+clientId+" and redirect to "+redirectUri);
		if (log.isDebugEnabled())
			log.debug("Using authorization code " + authCode);
		
		// validate
		if (authCode == null || authCode.trim().isEmpty()) 
			throw new IllegalArgumentException("authCode must not be null or empty");
		URLUtil.validateUrlParameter("RedirectURI", redirectUri);
		
		// trim values
		authCode = authCode.trim();
		
		// build access token specific params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("redirect_uri", redirectUri));
		params.add(new BasicNameValuePair("code", authCode));
		
		// call inner getToken with required token name
		return getTokenSetInner(params);
	}

	public TokenSet refreshTokens(String refreshToken) {
		if (log.isInfoEnabled())
			log.info("Refreshing tokens for client "+clientId);
		if (log.isDebugEnabled())
			log.debug("Using refresh token " + refreshToken);
		
		// validate
		if (refreshToken == null || refreshToken.trim().isEmpty()) 
			throw new IllegalArgumentException("refreshToken must not be null or empty");
		
		// trim values
		refreshToken = refreshToken.trim();
		
		// build access token specific params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
		params.add(new BasicNameValuePair("client_id", clientId));
		params.add(new BasicNameValuePair("refresh_token", refreshToken));
		
		// call inner getToken with required token name
		return getTokenSetInner(params);
	}
	
	/**
	 * Performs the request to the <code>token</code> end point.
	 * @param params POST request parameters to be send to the end point.
	 * @return Tokens received.
	 */
	protected TokenSetImpl getTokenSetInner(List<NameValuePair> params) {
		// create credentials provider for the basic authorization
		// using client credentials
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
        		AuthScope.ANY, 
        		new UsernamePasswordCredentials(clientId, clientSecret));
        
		try (CloseableHttpClient client = getHttpClientBuilder()
				.setDefaultCredentialsProvider(credsProvider)
				.build()) {
			
			if (log.isInfoEnabled())
				log.debug("Posting to token URL: "+this.tokenUrl);
			
			// create post reqest with required params
			HttpPost post = new HttpPost(this.tokenUrl);
			post.setEntity(new UrlEncodedFormEntity(params));
			
			// execute the post request to get the token
			CloseableHttpResponse response = client.execute(post);
			String entity = EntityUtils.toString(response.getEntity());
			
			// check if response is successful
            int authRespStatus = response.getStatusLine().getStatusCode();
            if (authRespStatus < 200 || authRespStatus >= 300) {
            	throw new ApiCallException(authRespStatus, response.getStatusLine().toString(), entity);
            }
            
            if (log.isDebugEnabled())
            	log.debug("Token response: "+entity);
			
			// We expect response entity to be a valid
			// JSON string and we extract the token set from it
            JSONObject jsonObj = new JSONObject(entity);
            
            // build token set
            TokenSetImpl tokenSet = new TokenSetImpl();
            tokenSet.setTimeCreated(LocalDateTime.now());
            if (jsonObj.has("expires_in"))
            	tokenSet.setExpiresInSeconds(jsonObj.getInt("expires_in"));
            if (jsonObj.has("access_token"))
            	tokenSet.setAccessToken(jsonObj.getString("access_token"));
            if (jsonObj.has("refresh_token"))
            	tokenSet.setRefreshToken(jsonObj.getString("refresh_token"));
            
            return tokenSet;
		} catch (ApiCallException ace) {
        	log.error("API call exception: "+ace.getMessage(), ace);
        	if (log.isDebugEnabled()) log.debug("Response content: "+ace.getResponseContent());
			throw ace;
		} catch (Exception e) {
			log.error("Request exception: "+e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * Returns configuration of HttpClientBuilder
	 * used to build the HttpClient instance.
	 */
	protected HttpClientBuilder getHttpClientBuilder() throws Exception {
		return HttpClientBuilder.create()
		.setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
		.setUserAgent("Mozilla/5.0")
		.setDefaultRequestConfig(
				RequestConfig.custom()
					.setCookieSpec(CookieSpecs.STANDARD)
					.build())
		.setSSLSocketFactory(
				new SSLConnectionSocketFactory(
						SSLContexts.custom()
							.loadTrustMaterial(null, new TrustSelfSignedStrategy())
							.build(),
						NoopHostnameVerifier.INSTANCE
						));
	}

}
