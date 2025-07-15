package com.talkjang.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.key}")
    private String JWT_SECRET_KEY;
    private final long EXPIRATION_TIME = Duration.ofDays(1).toMillis();

    public String createAccessToken(String userId){
        Date now = new Date();

        String jwt = Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();

        return jwt;
    }

    public String resolveAccessToken(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        if(token == null) return null;
        // 토큰이 Bearer 로 시작하는지 검증
        if(!token.startsWith("Bearer")) return null;

        // 해당 토큰이 위조되었는지, 만료됐는지 검증
        return (token.substring(7));
    }

    public boolean validateAccessToken(String token){

        try{
            Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e){
            log.info("JWT 토큰 검증 오류 : " + e);
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.get("userId").toString();

        return new UsernamePasswordAuthenticationToken
                ((userId), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
