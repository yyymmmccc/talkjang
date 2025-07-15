package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.user.UpdatePasswordRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 내정보가져오기, 내정보 수정
    @GetMapping("")
    public CommonResponseDto getUser(@AuthenticationPrincipal String userId){

        return userService.getUser(userId);
    }

    // 비밀번호 수정
    @PostMapping("/pwd-modify")
    public CommonResponseDto updatePassword(@AuthenticationPrincipal String userId,
                                            @RequestBody UpdatePasswordRequestDto dto){

        return userService.updatePassword(userId, dto);
    }
}
