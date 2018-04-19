package org.reso.web.api.client.wrapper;

import java.util.Map;

import org.json.JSONObject;
import org.reso.web.api.client.ResoClient;
import org.reso.web.api.client.ResoClient.ResponseFormat;

/**
 * {@link ResoClient} wrapper class that returns the 
 * response as <code>JSONObject</code> instance.
 * Requires the instance of {@link ResoClient} to be provided
 * as constructor argument.
 */
public class JsonResoClientWrapper {

	private final ResoClient resoClient;
	
	public JsonResoClientWrapper(ResoClient resoClient) {
		if (resoClient == null) throw new IllegalArgumentException("ResoClient must not be null");
		this.resoClient = resoClient;
	}
	
	public JSONObject getMetadata() {
		return new JSONObject(resoClient.getMetadata(ResponseFormat.JSON));
	}
	
	public JSONObject get(String request) {
		return new JSONObject(resoClient.get(request, ResponseFormat.JSON));
	}
	
	public JSONObject post(String request, Map<String, String> parameters) {
		return new JSONObject(resoClient.post(request, parameters, ResponseFormat.JSON));
	}
}
