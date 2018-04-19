package org.reso.web.api.openid;

import java.net.URI;

/**
 * Client for the OpenIDConnect service.
 * The class can be used to fully authorize the user
 * using his credentials as well as get the URI to the
 * external login page for user redirection.
 * Client also provides methods to receive access and refresh tokens
 * once user is authorized and refresh then once expired.
 *
 */
public interface OpenIDConnectClient {
	
	/**
	 * Overload of {@link #getAuthorizeURI(String, String)} with default {@code scope}.
	 */
	URI getAuthorizeURI(String redirectUri);
	
	/**
	 * Builds the authorization end point URI of
	 * the OpenIDConnect service.
	 * 
	 * @param redirectUri URI to redirect after successful login.
	 * @param scope Scope of authorization.
	 * @return
	 */
	URI getAuthorizeURI(String redirectUri, String scope);
	
	/**
	 * Overload of {@link #authorize(String, String, String, String)} with default {@code scope}.
	 */
	String authorize(String username, String password, String redirectUri);
	
	/**
	 * Performs full authorization against OpenIDConnect service.
	 * This includes invoking the <code>authorize</code> URL of the service
	 * and following the redirects till login page is retrieved.
	 * Method also fills the login form for the user by given credentials 
	 * and submits it. Finally the authorization <code>code</code> is read from
	 * the last redirect URL which OpenIDConnect service calls as callback.
	 * 
	 * @param username The name of the user to login.
	 * @param password The password of the user to login.
	 * @param redirectUri URI to redirect after successful login. The authorization <code>code</code> will be provided as parameter to this URL.
	 * @param scope Scope of authorization.
	 * @return The authorization <code>code</code> the service provides.
	 */
	String authorize(String username, String password, String redirectUri, String scope);
	
	/**
	 * Loads the access and refresh tokens from OpenIDConnect service
	 * once user is successfully authorized.
	 * 
	 * @param authCode The authorization <code>code</code> received using the <code>authorize</code> method.
	 * @param redirectUri URI to redirect to.
	 * @return Instance containing both access and refresh tokens.
	 */
	TokenSet getTokens(String authCode, String redirectUri);
	
	/**
	 * Refreshes the access and refresh tokens using OpenIDConnect service
	 * and current refresh token.
	 * 
	 * @param refreshToken Current refresh token.
	 * @return Instance containing both access and refresh tokens.
	 */
	TokenSet refreshTokens(String refreshToken);
	
}
