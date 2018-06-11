package org.reso.sdk.web.api.openid;

import java.util.Map;

/**
 * Class used to fill the login form of 
 * the authorization service.
 */
public interface LoginCommand {
	
	/**
	 * Action URL of the login form.
	 * @return URL String value.
	 */
	String getActionUrl();
	
	/**
	 * Login form request method.
	 * Usually POST.
	 * @return String value of method.
	 */
	String getMethod();
	
	/**
	 * Map containing input names and values
	 * from all input fields of the form.
	 * @return Map of name and value pairs.
	 */
	Map<String, String> getInputValues();

}
