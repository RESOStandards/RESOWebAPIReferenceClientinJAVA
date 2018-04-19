package org.reso.web.api.client.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.reso.web.api.client.ResoClient;
import org.reso.web.api.exception.ApiCallException;
import org.reso.web.api.store.TokenStore;
import org.reso.web.api.util.URLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the client.
 * It makes use of {@link TokenStore} and performs
 * requests against the <code>baseRequestUrl</code>.
 * This class is not intended to be instantiated directly.
 * Use {@link ResoClientBuilder} instead.
 */
public class ResoClientImpl implements ResoClient {
	
	private static final Logger log = LoggerFactory.getLogger(ResoClientImpl.class);
	
	private final String baseRequestUrl;
	private final TokenStore tokenStore;
	
	/**
	 * Default scoped constructor not to be invoked directly.
	 * Use {@link ResoClientBuilder} to create instances.
	 * 
	 * @param baseRequestUrl
	 * @param tokenStore
	 */
	ResoClientImpl(
			final String baseRequestUrl,
			final TokenStore tokenStore) {
		URLUtil.validateUrlParameter("BaseRequestURL", baseRequestUrl);
		if (tokenStore == null) throw new IllegalArgumentException("TokenStore must not be null");
		this.baseRequestUrl = baseRequestUrl;
		this.tokenStore = tokenStore;
	}
	
	@Override
	public String getMetadata(ResponseFormat format) {
		return requestInner(tokenStore.getValidAccessToken(), "$metadata", format, false, null);
	}
	
	@Override
	public String get(String request, ResponseFormat format) {
		return requestInner(tokenStore.getValidAccessToken(), request, format, false, null);
	}
	
	@Override
	public String post(String request, Map<String, String> parameters, ResponseFormat format) {
		return requestInner(tokenStore.getValidAccessToken(), request, format, true, parameters);
	}
	
	/**
	 * Implementation of request execution.
	 * @param accessToken Valid access token to set in the headers.
	 * @param request Request expression to be appended to <code>baseRequestUrl</code>.
	 * @param format Format to return the response in.
	 * @param post Is post request.
	 * @param parameters Post parameters map.
	 * @return Content of the response.
	 */
	protected String requestInner(String accessToken, String request, ResponseFormat format, boolean isPost, Map<String, String> parameters) {
		// validation
		if (accessToken == null)
			throw new IllegalArgumentException("Access token must not be null");
		if (request == null || request.trim().isEmpty())
			throw new IllegalArgumentException("Request must not be null or empty");
		
		// trim value
		request = request.trim();
		
		if (log.isInfoEnabled()) log.info((isPost ? "POST" : "GET")+" request for " + format + ": " + request);
		if (log.isDebugEnabled()) log.debug("Current access token: " + accessToken);
		
		// create HTTP client and execute request
		try (CloseableHttpClient client = HttpClientBuilder.create()
				.setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
				.setDefaultRequestConfig(
						RequestConfig.custom()
							.setCookieSpec(CookieSpecs.DEFAULT)
							.build())
				.build()) {
        	
			// build final URI
			URI finalURI = buildFinalURI(request);
    		if (log.isDebugEnabled()) log.debug("Final request URI: " + finalURI);
    		
    		// Create GET or POST request
    		HttpRequestBase req = null;
    		if (isPost) {
    			HttpPost post = new HttpPost(finalURI);
    			
    			// add all parameters to post request
    			if (parameters != null && parameters.size() > 0) {
    				List<NameValuePair> postParams = parameters.entrySet()
    						.stream()
    						.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
    						.collect(Collectors.toList());
    				if (log.isInfoEnabled())
    					log.info("POST parameters: "+postParams);
    				post.setEntity(new UrlEncodedFormEntity(postParams));
    			}
    			
    			req = post;
    		}
    		else req = new HttpGet(finalURI);
    		
    		// add authorization and accept headers
            req.addHeader(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken));
            req.addHeader(new BasicHeader(HttpHeaders.ACCEPT, format.getAccept()));
            
            if (log.isDebugEnabled())
            	log.debug("Request headers: " + Arrays.asList(req.getAllHeaders()).stream().map(h -> h.toString()).collect(Collectors.joining(", ")));
            
            // Execute request
            CloseableHttpResponse response = client.execute(req);
            String content = EntityUtils.toString(response.getEntity());
            
            if (log.isDebugEnabled())
            	log.debug("Response: " + response);
            
            // check if response is successful
            int authRespStatus = response.getStatusLine().getStatusCode();
            if (authRespStatus < 200 || authRespStatus >= 300) {
            	throw new ApiCallException(authRespStatus, response.getStatusLine().toString(), content);
            }
        	
        	if (log.isDebugEnabled()) log.debug("Response content " + format + ": " + content);
        	
        	return content;
            
        } catch (ApiCallException ace) {
        	log.error("API call exception: "+ace.getMessage(), ace);
        	if (log.isDebugEnabled()) log.debug("Response content: "+ace.getResponseContent());
			throw ace;
		} catch (Exception e) {
			log.error("Request exception: "+e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	protected URI buildFinalURI(String request) {
		try {
			String query = "";
			String finalUrl = baseRequestUrl;
			
			if (!finalUrl.endsWith("/")) finalUrl += "/";
			if (request.startsWith("/")) request = request.substring(1);
			
			// separate query from url
			if (request.contains("?")) {
				int idx = request.indexOf("?");
				finalUrl += request.substring(0, idx); 
				query = request.substring(idx+1);
			} else {
				finalUrl += request;
			}
			
			// final URI builder
			URIBuilder uri = new URIBuilder(finalUrl);
			
			// parse query into parameters
			if (!query.isEmpty()) {
				List<NameValuePair> finalParams = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
				finalParams.stream().forEach(param -> {
					uri.addParameter(param.getName(), param.getValue());
				});
			}
			
			return uri.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Unable to build final request URI from request: " + request);
		}
	}

}
