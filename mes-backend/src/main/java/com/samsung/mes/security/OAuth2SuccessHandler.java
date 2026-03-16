package com.samsung.mes.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.samsung.mes.entity.Member;
import com.samsung.mes.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2 로그인 성공 시: DB에 회원 저장/조회 → JWT 발급 → 프론트엔드로 리다이렉트 (?token=xxx)
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final MemberService memberService;
	private final JwtUtil jwtUtil;

	@Value("${frontend.url:http://localhost:5174}")
	private String frontendUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> attrs = oauth2User.getAttributes();

		String registrationId = getRegistrationId(request);
		String providerId = getProviderId(attrs, registrationId);
		String email = getEmail(attrs, registrationId);
		String name = getName(attrs, registrationId);

		Member member = memberService.findOrCreateOAuthUser(registrationId, providerId, email, name);
		String token = jwtUtil.createToken(member.getEmail());

		String fn = member.getFirstName() != null ? URLEncoder.encode(member.getFirstName(), StandardCharsets.UTF_8) : "";
		String ln = member.getLastName() != null ? URLEncoder.encode(member.getLastName(), StandardCharsets.UTF_8) : "";
		String redirectUrl = frontendUrl + "/?token=" + token + "&firstName=" + fn + "&lastName=" + ln;
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

	private String getRegistrationId(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.contains("/oauth2/code/google")) return "google";
		if (uri.contains("/oauth2/code/kakao")) return "kakao";
		if (uri.contains("/oauth2/code/naver")) return "naver";
		return "unknown";
	}

	private String getProviderId(Map<String, Object> attrs, String provider) {
		return switch (provider) {
			case "google" -> (String) attrs.getOrDefault("sub", "");
			case "kakao" -> String.valueOf(attrs.getOrDefault("id", ""));
			case "naver" -> {
				@SuppressWarnings("unchecked")
				Map<String, Object> res = (Map<String, Object>) attrs.get("response");
				yield res != null ? String.valueOf(res.getOrDefault("id", "")) : "";
			}
			default -> "";
		};
	}

	private String getEmail(Map<String, Object> attrs, String provider) {
		return switch (provider) {
			case "google" -> (String) attrs.getOrDefault("email", "");
			case "kakao" -> {
				@SuppressWarnings("unchecked")
				Map<String, Object> acc = (Map<String, Object>) attrs.get("kakao_account");
				String e = acc != null ? (String) acc.get("email") : null;
				yield e != null ? e : "kakao_" + attrs.get("id") + "@kakao.local";
			}
			case "naver" -> {
				@SuppressWarnings("unchecked")
				Map<String, Object> res = (Map<String, Object>) attrs.get("response");
				String e = res != null ? (String) res.get("email") : null;
				yield e != null ? e : "naver_" + (res != null ? res.get("id") : "") + "@naver.local";
			}
			default -> "";
		};
	}

	private String getName(Map<String, Object> attrs, String provider) {
		return switch (provider) {
			case "google" -> (String) attrs.getOrDefault("name", "");
			case "kakao" -> {
				@SuppressWarnings("unchecked")
				Map<String, Object> props = (Map<String, Object>) attrs.get("properties");
				String n = props != null ? (String) props.get("nickname") : null;
				yield n != null ? n : "";
			}
			case "naver" -> {
				@SuppressWarnings("unchecked")
				Map<String, Object> res = (Map<String, Object>) attrs.get("response");
				String n = res != null ? (String) res.get("name") : null;
				yield n != null ? n : "";
			}
			default -> "";
		};
	}
}
