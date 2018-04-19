package org.reso.web.api.client.wrapper;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.reso.web.api.client.ResoClient;
import org.reso.web.api.client.ResoClient.ResponseFormat;
import org.w3c.dom.Document;

/**
 * {@link ResoClient} wrapper class that returns the 
 * response as XML <code>Document</code> instance.
 * Requires the instance of {@link ResoClient} to be provided
 * as constructor argument.
 */
public class XmlResoClientWrapper {

	private final ResoClient resoClient;
	
	public XmlResoClientWrapper(ResoClient resoClient) {
		if (resoClient == null) throw new IllegalArgumentException("ResoClient must not be null");
		this.resoClient = resoClient;
	}
	
	public Document getMetadata() {
		return xmlStringToDocument(resoClient.getMetadata(ResponseFormat.XML));
	}
	
	public Document get(String request) {
		return xmlStringToDocument(resoClient.get(request, ResponseFormat.XML));
	}
	
	public Document post(String request, Map<String, String> parameters) {
		return xmlStringToDocument(resoClient.post(request, parameters, ResponseFormat.XML));
	}
	
	/**
	 * Transforms XML string content to Document.
	 * @param content XML String.
	 * @return XML Document.
	 */
	protected Document xmlStringToDocument(String content) {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return db.parse(new ByteArrayInputStream(content.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
