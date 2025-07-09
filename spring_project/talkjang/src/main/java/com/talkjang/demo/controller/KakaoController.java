package com.talkjang.demo.controller;

import com.talkjang.demo.dto.response.CommonResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/kakao/test")
public class KakaoController {

    @GetMapping("")
    public CommonResponseDto test(){

        RestTemplate restTemplate = new RestTemplate();
        String a = String.valueOf(restTemplate.getForEntity("https://kauth.kakao.com/oauth/authorize", String.class));

        return CommonResponseDto.success(a);
    }
}
