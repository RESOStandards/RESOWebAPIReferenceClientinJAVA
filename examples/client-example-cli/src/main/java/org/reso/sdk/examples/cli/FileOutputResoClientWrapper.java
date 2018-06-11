package org.reso.sdk.examples.cli;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.client.ResoClient.ResponseFormat;

public class FileOutputResoClientWrapper {
	
	private final ResoClient resoClient;
	
	public FileOutputResoClientWrapper(ResoClient resoClient) {
		if (resoClient == null) throw new IllegalArgumentException("ResoClient must not be null");
		this.resoClient = resoClient;
	}
	
	public void getMetadata(ResponseFormat format, String fileName) {
		writeToFile(resoClient.getMetadata(format), fileName);
	}
	
	public void get(String request, ResponseFormat format, String fileName) {
		writeToFile(resoClient.get(request, format), fileName);
	}
	
	public void post(String request, Map<String, String> parameters, ResponseFormat format, String fileName) {
		writeToFile(resoClient.post(request, parameters, format), fileName);
	}
	
	private void writeToFile(String content, String fileName) {
		try {
			Files.write(Paths.get(fileName), content.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
