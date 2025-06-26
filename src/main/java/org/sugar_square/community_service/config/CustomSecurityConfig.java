package org.sugar_square.community_service.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {

  /*
   * TODO : 로그인 필터 추가, pw 인코딩 추가, 인증 시 jwt 토큰 발급,
   * jwt 인증, 인가 필터는 spring cloud gateway 에서 구현해야 함
   * 로그인 폼 -> gateway pass -> 인증 서버에서 로그인, jwt 발급 -> gateway pass -> 클라이언트에 jwt 전달
   * 이후 모든 request 는 gateway 에서 jwt 인증 필터를 거쳐 인증
   * */

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // 모든 path 에 대해 인증 요구 x
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // 정적 리소스에 대해 security 필터 적용 x
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .requestMatchers("/api/members/signup", "/api/members/check-duplication");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
