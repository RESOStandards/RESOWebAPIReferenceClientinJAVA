package org.reso.web.api.client.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.reso.web.api.client.impl.ResoClientBuilder;
import org.reso.web.api.client.impl.ResoClientImpl;
import org.reso.web.api.store.impl.DefaultTokenStore;
import org.reso.web.api.util.TestFileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlResoClientWrapperTest {
	
	@Test
	public void constructorValidationTest() {
		try {
			new XmlResoClientWrapper(null);
			fail("Validation exception not thrown");
		} catch (Exception e) {
			assertTrue("Wrong message", e.getMessage().contains("ResoClient must not be null"));
		}
	}
	
	@Test
	public void xmlStringToDocumentTest() {
		ResoClientImpl reso = buildClient();
		XmlResoClientWrapper wrapper = new XmlResoClientWrapper(reso);
		// read content from file
		String content = TestFileUtil.readFile("metadata.xml");
		assertNotNull("Content is null", content);
		// parse doc
		Document doc = wrapper.xmlStringToDocument(content);
		// test the doc
		assertNotNull("Document is null", doc);
		Element root = doc.getDocumentElement();
		assertNotNull("Root element is null", root);
		assertEquals("Wrong node name", "service", root.getNodeName());
	}
	
	private ResoClientImpl buildClient() {
		return (ResoClientImpl) ResoClientBuilder
				.create()
				.setBaseRequestUrl("http://someurl")
				.setTokenStore(new DefaultTokenStore())
				.build();
	}
	
	
	
}
