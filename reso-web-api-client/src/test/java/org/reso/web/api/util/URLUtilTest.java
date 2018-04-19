package org.reso.web.api.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class URLUtilTest {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testValid() {
		URLUtil.validateUrlParameter("Name", "http://something/here");
		URLUtil.validateUrlParameter("Name", "https://something");
		URLUtil.validateUrlParameter("Name", "http://something:333");
		URLUtil.validateUrlParameter("Name", "https://something:333/here/and/here");
		URLUtil.validateUrlParameter("Name", "http://something:333/here?param=and");
		URLUtil.validateUrlParameter("Name", "HTTP://something:333");
		URLUtil.validateUrlParameter("Name", "HTTPS://something:333");
	}
	
	@Test
	public void testNotValidFtp() {
		thrown.expect(IllegalArgumentException.class);
		URLUtil.validateUrlParameter("Name", "ftp://something");
	}
	
	@Test
	public void testNotValidtext() {
		thrown.expect(IllegalArgumentException.class);
		URLUtil.validateUrlParameter("Name", "something here");
	}
	
}
