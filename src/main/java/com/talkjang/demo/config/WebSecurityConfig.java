package com.talkjang.demo.config;

import com.talkjang.demo.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource;
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity // cors -> cors 설정은 내가 설정한 urlBased 메서드사
                .cors(cors -> cors.configurationSource(urlBasedCorsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/upload/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/shop/**").permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                                .anyRequest().permitAll()
                        //.anyRequest().authenticated()
                )

        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }
}
