package org.reso.web.api.openid.impl;

import java.util.Map;

import org.reso.web.api.openid.LoginCommand;

/**
 * Default <code>LoginCommand</code> implementation.
 */
public class LoginCommandImpl implements LoginCommand {

	private String actionUrl;
	private String method;
	private Map<String, String> inputValues;
	
	/**
	 * Gets action URL.
	 */
	public String getActionUrl() {
		return actionUrl;
	}
	
	/**
	 * Sets the action URL.
	 * @param actionUrl
	 */
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	/**
	 * Gets the method.
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * Sets the method.
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * Gets the input name and value pairs.
	 */
	public Map<String, String> getInputValues() {
		return inputValues;
	}
	
	/**
	 * Sets the input name and value pairs.
	 * @param inputValues
	 */
	public void setInputValues(Map<String, String> inputValues) {
		this.inputValues = inputValues;
	}
	
}
