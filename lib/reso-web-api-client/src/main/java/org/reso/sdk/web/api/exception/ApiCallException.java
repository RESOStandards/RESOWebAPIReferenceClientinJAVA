package org.reso.sdk.web.api.exception;

public class ApiCallException extends RuntimeException {
	private static final long serialVersionUID = 5318189168640873317L;

	private int httpStatusCode;
	private String responseContent;
	
	public ApiCallException(int httpStatusCode, String message, String responseContent) {
		super(message);
		this.httpStatusCode = httpStatusCode;
		this.responseContent = responseContent;
	}
	
	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	public String getResponseContent() {
		return responseContent;
	}
	
}
