package com.samsung.mes.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.samsung.mes.security.OAuth2FailureHandler;
import com.samsung.mes.security.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;
	// PasswordEncoder는 PasswordEncoderConfig로 분리 (순환 참조 방지)

	//cors
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		//쿠키/세션/인증정보(Authorization 헤더 포함 등)를 브라우저 요청에 포함하는 걸 허용
		config.setAllowedOriginPatterns(List.of("http://localhost:5173", "http://localhost:5174"));
		//“React 개발 서버(5173)에서 오는 요청만 허용할게”
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		//허용할 HTTP 메서드 목록 (GET/POST/PUT/DELETE/OPTIONS)
		config.setAllowedHeaders(List.of("*"));//프론트가 보내는 헤더는 전부 허용
//예: Content-Type, Authorization 등
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);//모든 경로에 cors허용
		return source;
	}

	//실제 보안규칙 설정 (누가 어떤 API접근 가능)
	//@Override what 뭘 재정의 하는데
	//그래서 콩으로 등록
	@Bean//이 메서드가 만든 결과를 스프링이 관리하는 공식설정으로 등록해줘
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//SecurityFilterChain => 검사하는 규칙들에 줄, 보안 검사 규칙세트 어떤 요청은 통과하고 어떤 요청은 막을지..
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.formLogin(form -> form.disable())
				.httpBasic(basic -> basic.disable())
				.oauth2Login(oauth2 -> oauth2
						.successHandler(oAuth2SuccessHandler)
						.failureHandler(oAuth2FailureHandler)
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/members/login").permitAll()
						.requestMatchers("/members/login","/members/register","/members/logout").permitAll()
						.requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
						.requestMatchers("/api/**").permitAll()
						.requestMatchers("/api/sales/orders/**").permitAll()
						.requestMatchers("/", "/error", "/favicon.ico").permitAll()
						.anyRequest().authenticated()
				);
		return http.build();
//지금까지 설정한 보안 규칙을 최종 완성해서 서버에 적용
	}
}