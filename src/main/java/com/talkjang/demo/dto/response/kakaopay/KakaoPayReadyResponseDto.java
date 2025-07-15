package com.talkjang.demo.dto.response.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyResponseDto {

    private String tid;
    private String next_redirect_pc_url;
}
