package org.reso.sdk.web.api.openid;

/**
 * Abstract adapter for login form parsing and filling.
 * Concrete implementations may be used for specific
 * login forms.
 */
public interface LoginFormAdapter {

	/**
	 * Parses login form and fills form with 
	 * user credentials provided.
	 * 
	 * @param content String login page content.
	 * @param username Login name of the user.
	 * @param password Login password of the user.
	 * @return Filled login command object.
	 */
	LoginCommand parseAndFill(String content, String username, String password);
	
}
