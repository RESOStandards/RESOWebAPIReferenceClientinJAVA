package org.reso.sdk.examples.web;

import org.reso.sdk.web.api.client.ResoClient;
import org.reso.sdk.web.api.client.ResoClient.ResponseFormat;
import org.reso.sdk.web.api.openid.OpenIDConnectClient;
import org.reso.sdk.web.api.store.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	@Autowired
	OpenIDConnectClient openIDClient;
	
	@Autowired
	TokenStore tokenStore;
	
	@Autowired
	ResoClient resoClient;
	
	@Value("${redirectUri}")
	String redirectUri;
	
	@Value("${scope}")
	String scope;
	
	@RequestMapping("/")
	public String root() {
		if (!isAuthorized()) return "redirect:login";
		return "redirect:request";
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			Model model) {
		try {
			// get auth code
			String authCode = openIDClient.authorize(username, password, redirectUri, scope);
			// store tokens
			tokenStore.storeTokens(openIDClient.getTokens(authCode, redirectUri));
		} catch (Exception e) {
			return "redirect:login?error";
		}
		return "redirect:request";
	}
	
	@RequestMapping(value="request", method=RequestMethod.GET)
	public String requestForm() {
		if (!isAuthorized()) return "redirect:login";
		return "request";
	}
	
	@RequestMapping(value="request", method=RequestMethod.POST)
	public String request(
			@RequestParam("request") String request, 
			@RequestParam("format") ResponseFormat format,
			Model model) {
		String content = resoClient.get(request, format);
		model.addAttribute("content", content);
		return "request";
	}
	
	private boolean isAuthorized() {
		try {
			tokenStore.getValidAccessToken();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
