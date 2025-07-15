package com.talkjang.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfiguration() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8100");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 프론트에서 localhost:8080/image/ 로 시작하는 모든 요청을 users/myungchul-yoon~~ 에서 찾는다는말
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:/Users/myungchul-yoon/talkjang_upload/images/");
    }
}
