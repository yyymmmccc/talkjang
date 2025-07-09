package com.talkjang.demo.controller;

import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.chat.CreateChatRoomResponseDto;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create/{toUserId}")
    public CommonResponseDto createChatRoom(@AuthenticationPrincipal String userId,
                                            @PathVariable("toUserId") String toUserId){

        return chatRoomService.createChatRoomWithResponse(userId, toUserId);
    }

    // 해당 사용자에 채팅목록가져오기
    @GetMapping("/list")
    public CommonResponseDto getChatRoomList(@AuthenticationPrincipal String userId){

        return chatRoomService.getChatRoomList(userId);
    }
}
