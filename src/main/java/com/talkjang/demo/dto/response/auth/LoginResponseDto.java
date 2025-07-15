package com.talkjang.demo.dto.response.auth;

import com.talkjang.demo.dto.request.auth.LoginRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String redirect;

    public static LoginResponseDto of(String accessToken, String redirect){
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .redirect(redirect)
                .build();
    }
}
