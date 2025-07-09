package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.auth.LoginRequestDto;
import com.talkjang.demo.dto.request.auth.UserCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/dup-id")
    public CommonResponseDto checkUserIdDuplicate(@RequestParam("userId") String userId){
        return authService.checkUserIdDuplicate(userId);
    }

    @GetMapping("/dup-shopName")
    public CommonResponseDto checkShopNameDuplicate(@RequestParam("shopName") String shopName){
        return authService.checkShopNameDuplicate(shopName);
    }

    @GetMapping("/dup-email")
    public CommonResponseDto checkUserEmailDuplicate(@RequestParam("email") String email){
        return authService.checkUserEmailDuplicate(email);
    }

    @GetMapping("/dup-phone")
    public CommonResponseDto checkUserPhoneDuplicate(@RequestParam("phoneNumber") String phoneNumber){
        return authService.checkUserPhoneDuplicate(phoneNumber);
    }

    @PostMapping("/create")
    public CommonResponseDto create(@RequestBody UserCreateRequestDto dto){
        return authService.create(dto);
    }

    @PostMapping("/login")
    public CommonResponseDto login(@RequestBody LoginRequestDto dto){

        return authService.login(dto);
    }
}
