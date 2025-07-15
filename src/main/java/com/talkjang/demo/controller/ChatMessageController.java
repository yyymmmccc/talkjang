package com.talkjang.demo.controller;

import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-message")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{roomId}")
    public CommonResponseDto getChatMessagesByRoomId(@AuthenticationPrincipal String userId,
                                                     @PathVariable("roomId") String roomId){

        return chatMessageService.getChatMessagesByRoomId(userId, roomId);
    }

    @PatchMapping("/{roomId}/read-at")
    public void updateLastReadMessageAt(@AuthenticationPrincipal String userId,
                                        @PathVariable("roomId") String roomId){

        chatMessageService.updateLastReadMessageAt(userId, roomId);
    }
}
