package org.reso.web.api.openid.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.reso.web.api.util.TestFileUtil;

public class DefaultLoginFormAdapterTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	DefaultLoginFormAdapter adapter = new DefaultLoginFormAdapter("http://something/here");
	
	@Test
	public void testConstructorNullAuthorizeUrl() {
		thrown.expect(IllegalArgumentException.class);
		new DefaultLoginFormAdapter(null);
	}
	
	@Test
	public void testConstructorInvalidAuthorizeUrl() {
		thrown.expect(IllegalArgumentException.class);
		new DefaultLoginFormAdapter("something here");
	}
	
	@Test
	public void testParseLoginForm() {
		String content = TestFileUtil.readFile("login.html");
		LoginCommandImpl cmd = adapter.parseLoginForm(content);
		assertEquals("wrong action url", "/ticket/login", cmd.getActionUrl());
		assertEquals("wrong method", "post", cmd.getMethod());
		assertTrue("Wrong number of inputs", cmd.getInputValues().size() == 3);
		assertEquals("wrong input someinput value", "somevalue", cmd.getInputValues().get("someinput"));
	}
	
	@Test
	public void testParseLoginFormNoForm() {
		thrown.expect(IllegalArgumentException.class);
		String content = TestFileUtil.readFile("login-no-form.html");
		adapter.parseLoginForm(content);
	}
	
	@Test
	public void testFillLoginForm() {
		String content = TestFileUtil.readFile("login.html");
		LoginCommandImpl cmd = adapter.parseLoginForm(content);
		cmd = adapter.fillLoginCommand(cmd, "someuser", "somepass");
		assertEquals("wrong final action url", "http://something/ticket/login", cmd.getActionUrl());
		assertEquals("wrong input username value", "someuser", cmd.getInputValues().get("user"));
		assertEquals("wrong input password value", "somepass", cmd.getInputValues().get("password"));
	}
	
	@Test
	public void testSetNullUsernameInputNames() {
		thrown.expect(IllegalArgumentException.class);
		adapter.setValidInputNamesUsername(null);
	}
	
	@Test
	public void testSetNullPasswordInputNames() {
		thrown.expect(IllegalArgumentException.class);
		adapter.setValidInputNamesPassword(null);
	}
	
	@Test
	public void testParseAndFillNullContent() {
		thrown.expect(IllegalArgumentException.class);
		adapter.parseAndFill(null, "username", "password");
	}
	
	@Test
	public void testParseAndFillNullUsername() {
		thrown.expect(IllegalArgumentException.class);
		adapter.parseAndFill("content", null, "password");
	}
	
	@Test
	public void testParseAndFillEmptyUsername() {
		thrown.expect(IllegalArgumentException.class);
		adapter.parseAndFill("content", " ", "password");
	}
	
	@Test
	public void testParseAndFillNullPassword() {
		thrown.expect(IllegalArgumentException.class);
		adapter.parseAndFill("content", "username", null);
	}
	
	@Test
	public void testParseAndFillEmptyPassword() {
		thrown.expect(IllegalArgumentException.class);
		adapter.parseAndFill("content", "username", " ");
	}
}
