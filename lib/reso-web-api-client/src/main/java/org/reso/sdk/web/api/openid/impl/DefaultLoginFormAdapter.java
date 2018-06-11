package org.reso.sdk.web.api.openid.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.reso.sdk.web.api.openid.LoginCommand;
import org.reso.sdk.web.api.openid.LoginFormAdapter;
import org.reso.sdk.web.api.util.URLUtil;

/**
 * Default implementation of <code>LoginFormAdapter</code>.
 * Parses login form content and fills with user credentials.
 * When form action URL starts from context root, it is prefixed
 * with the main part of <code>authorizeUrl</code> value.
 * To fill the user name value this implementation searched for one of
 * the valid user name fields in the form.
 * To fill the password value this implementation searches for one of
 * the valid password fields in the form. 
 */
public class DefaultLoginFormAdapter implements LoginFormAdapter {
	
	private String authorizeUrl;
	
	private List<String> validInputNamesUsername = Arrays.asList("username", "j_username", "user", "email");
	
	private List<String> validInputNamesPassword = Arrays.asList("password", "j_password", "pass");
	
	/**
	 * Constructor.
	 * @param authorizeUrl Value of authorize URL.
	 */
	public DefaultLoginFormAdapter(String authorizeUrl) {
		URLUtil.validateUrlParameter("AuthorizeURL", authorizeUrl);
		this.authorizeUrl = authorizeUrl;
	}

	/**
	 * Implementation of interface method.
	 */
	@Override
	public LoginCommand parseAndFill(String content, String username, String password) {
		// validate
		if (content == null) throw new IllegalArgumentException("content must not be null");
		if (username == null || username.trim().isEmpty()) 
			throw new IllegalArgumentException("username must not be null or empty");
		if (password == null || password.trim().isEmpty()) 
			throw new IllegalArgumentException("password must not be null or empty");
		
		LoginCommandImpl parsed = parseLoginForm(content);
		return fillLoginCommand(parsed, username.trim(), password.trim());
	}
	
	/**
	 * Parses login form to build the initial login command object.
	 * @param loginPage String content of the login page.
	 * @return Initial login command.
	 */
	protected LoginCommandImpl parseLoginForm(String loginPage) {
		// parse HTML document
		Document doc = Jsoup.parse(loginPage);
		if (doc == null) throw new IllegalArgumentException("Unable to parse login page content");
		
		// find first form in the login page
		// and parse required attributes
		LoginCommandImpl params = new LoginCommandImpl();
		Element form = doc.selectFirst("form");
		if (form == null) throw new IllegalArgumentException("Login page does not have a form");
		
		params.setActionUrl(form.attr("action"));
		params.setMethod(form.attr("method"));
		
		// read all input fields from the form
		// and set them with values to param map
		params.setInputValues(new HashMap<String, String>());
		form.select("input").forEach(input -> {
			params.getInputValues().put(input.attr("name"), input.attr("value"));
		});
		
		return params;
	}
	
	/**
	 * Fills the initial login command with user credentials.
	 * @param loginParams Initial login command parsed from the page.
	 * @param username Value of user name to fill.
	 * @param password Value of password to fill.
	 * @return Filled instance of <code>LoginCommand</code>.
	 */
	protected LoginCommandImpl fillLoginCommand(LoginCommandImpl loginParams, String username, String password) {
		// fix action URL
        String url = loginParams.getActionUrl();
        
        // if action URL is not a full URL
        // but relative to context root
        // we add the missing part
        if (!url.contains("://")) {
        	StringBuilder newUrl = new StringBuilder(authorizeUrl.substring(0, authorizeUrl.indexOf("/", authorizeUrl.indexOf("://") + 3)));
        	if (!url.startsWith("/")) newUrl.append("/");
        	url = newUrl.append(url).toString();
        }
        
        // set fixed action URL to the login params.
        loginParams.setActionUrl(url);
        
        // now we must find username and password
        // input fields and fill them with values
        // we inspect all valid field names
        for (String inputName : loginParams.getInputValues().keySet()) {
			if (validInputNamesUsername.contains(inputName))
				loginParams.getInputValues().put(inputName, username);
			if (validInputNamesPassword.contains(inputName))
				loginParams.getInputValues().put(inputName, password);
		}
        
        return loginParams;
	}

	/**
	 * Get the authorize URL.
	 * @return Authorize URL String value.
	 */
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	/**
	 * Get valid user name input field names.
	 * Default values are "username", "j_username", "user", "email".
	 * @return List of valid values.
	 */
	public List<String> getValidInputNamesUsername() {
		return validInputNamesUsername;
	}

	/**
	 * Set the valid user name input field names.
	 * @param validInputNamesUsername List of values.
	 */
	public void setValidInputNamesUsername(List<String> validInputNamesUsername) {
		if (validInputNamesUsername == null) 
			throw new IllegalArgumentException("validInputNamesUsername must not be null");
		this.validInputNamesUsername = validInputNamesUsername;
	}

	/**
	 * Get valid password input field names.
	 * Default values are "password", "j_password", "pass".
	 * @return List of valid values.
	 */
	public List<String> getValidInputNamesPassword() {
		return validInputNamesPassword;
	}
	
	/**
	 * Set the valid password input field names.
	 * @param validInputNamesPassword List of values.
	 */
	public void setValidInputNamesPassword(List<String> validInputNamesPassword) {
		if (validInputNamesPassword == null) 
			throw new IllegalArgumentException("validInputNamesPassword must not be null");
		this.validInputNamesPassword = validInputNamesPassword;
	}
	
}
