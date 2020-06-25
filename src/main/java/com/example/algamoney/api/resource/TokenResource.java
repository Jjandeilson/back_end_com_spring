package com.example.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.config.property.AplicacaoApiProperty;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	@Autowired
	private AplicacaoApiProperty aplicacaoApiProperty;

	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshToken = new Cookie("refreshToken", null);
		refreshToken.setHttpOnly(true);
		refreshToken.setSecure(aplicacaoApiProperty.getSeguranca().isEnableHttps());
		refreshToken.setPath(request.getContextPath() + "/oauth/token");
		refreshToken.setMaxAge(0);
		
		response.addCookie(refreshToken);
		response.setStatus(HttpStatus.NO_CONTENT.value());
	}
}
