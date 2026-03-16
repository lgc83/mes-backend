package com.samsung.mes.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 설정 여부 확인 API.
 * 프론트엔드에서 소셜 로그인 버튼 활성화 여부 판단용.
 */
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

	@Value("${spring.security.oauth2.client.registration.google.client-id:}")
	private String googleClientId;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id:}")
	private String kakaoClientId;

	@Value("${spring.security.oauth2.client.registration.naver.client-id:}")
	private String naverClientId;

	@GetMapping("/status")
	public ResponseEntity<Map<String, Boolean>> getOAuthStatus() {
		Map<String, Boolean> status = new HashMap<>();
		status.put("google", isConfigured(googleClientId, "your-google-client-id"));
		status.put("kakao", isConfigured(kakaoClientId, "your-kakao-rest-api-key"));
		status.put("naver", isConfigured(naverClientId, "your-naver-client-id"));
		return ResponseEntity.ok(status);
	}

	private boolean isConfigured(String value, String placeholder) {
		return value != null && !value.isBlank() && !value.equals(placeholder);
	}
}
