package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.kakaopay.OrderInfoRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kakao-pay")
@RequiredArgsConstructor
@Slf4j
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public CommonResponseDto ready(@AuthenticationPrincipal String userId,
                                   @RequestBody OrderInfoRequestDto orderInfoRequestDto){

        return kakaoPayService.ready(userId, orderInfoRequestDto);
    }

    @GetMapping("/approve")
    public ResponseEntity approve(@RequestParam("pg_token") String pgToken,
                                  @RequestParam("orderId") String orderId,
                                  @RequestParam("userId") String userId){

        return kakaoPayService.approve(pgToken, orderId, userId);
    }

    @GetMapping("/fail")
    public CommonResponseDto fail(@RequestParam("orderId") Long orderId){

        return null;
    }

    @GetMapping("/cancel")
    public CommonResponseDto cancel(@RequestParam("orderId") Long orderId){

        return kakaoPayService.cancel(orderId);
    }
}
