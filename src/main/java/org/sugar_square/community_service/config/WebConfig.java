package org.sugar_square.community_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  // TODO: api.sugar-square.org 서브 도메인 사용 시 수정할 가능성 있음
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("/api", c ->
        c.getPackageName().startsWith("org.sugar_square.community_service.controller")
    );
  }

  // TODO : Next.js 서버의 도메인에 따라 CORS 설정을 변경해야 할 수 있음
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")  // CORS를 적용할 경로 패턴
        .allowedOrigins("https://sugar-square.org", "http://localhost:3000")  // 허용할 출처
        .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
        .allowedHeaders("*")  // 허용할 헤더
        .allowCredentials(true);  // 쿠키 등 인증정보 허용 여부
  }
}
