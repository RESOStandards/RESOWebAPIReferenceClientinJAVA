package org.reso.web.api.client;

import java.util.Map;

/**
 * Client interface to execute RESO requests.
 * Performs basic HTTP get and post requests.
 * Returns response content as String in a requested format.
 */
public interface ResoClient {
	
	/**
	 * Enumeration of available response types.
	 * Each response type provides specific accept header value
	 * which is provided to the requests upon execution.
	 */
	public enum ResponseFormat {
		
		DEFAULT("*/*"),
		
		XML("application/xml"),
		
		JSON("application/json");
		
		private String accept;
		
		/**
		 * Constructor to set accept value.
		 * @param accept Accept value.
		 */
		private ResponseFormat(String accept) {
			this.accept = accept;
		}
		
		/**
		 * Gets the accept header for this format.
		 * @return Accept header value.
		 */
		public String getAccept() {
			return this.accept;
		}
	}
	
	/**
	 * Gets the meta data form the service.
	 * @param format Response format to return content in.
	 * @return Response content as String.
	 */
	public String getMetadata(ResponseFormat format);
	
	/**
	 * Executes a get request on service end point.
	 * @param request Request expression to execute.
	 * @param format Response format to return content in.
	 * @return Response content as String.
	 */
	public String get(String request, ResponseFormat format);
	
	/**
	 * Executes a post request on service end point.
	 * @param request Request expression to execute.
	 * @param parameters Map of post request parameters.
	 * @param format Response format to return content in.
	 * @return Response content as String.
	 */
	public String post(String request, Map<String, String> parameters, ResponseFormat format);
	
}
