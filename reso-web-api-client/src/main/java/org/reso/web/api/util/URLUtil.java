package org.reso.web.api.util;

import java.net.URL;

/**
 * Utility class for URLs
 */
public class URLUtil {
	
	/**
	 * Validates URL argument.
	 * @param name Name of the argument.
	 * @param url String URL.
	 * @throws IllegalArgumentException if URL is not valid.
	 */
	public static void validateUrlParameter(String name, String url) {
		try {
			new URL(url).toURI();
		} catch (Exception e) {
			throw new IllegalArgumentException(name + " " + url + " is not valid URI: "+e.getMessage(), e);
		}
		url = url.trim().toLowerCase();
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			throw new IllegalArgumentException("Only http or https URL is allowed");
	}
	
}
