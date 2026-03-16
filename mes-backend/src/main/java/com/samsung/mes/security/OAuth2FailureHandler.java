package com.samsung.mes.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OAuth2 로그인 실패 시 프론트엔드 로그인 페이지로 리다이렉트 (?error=oauth_failed)
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Value("${frontend.url:http://localhost:5174}")
	private String frontendUrl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String redirectUrl = frontendUrl + "/?error=oauth_failed&message="
				+ java.net.URLEncoder.encode(exception.getMessage() != null ? exception.getMessage() : "소셜 로그인에 실패했습니다.", java.nio.charset.StandardCharsets.UTF_8);
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}
}
