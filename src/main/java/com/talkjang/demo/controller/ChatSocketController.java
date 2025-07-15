package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.chat.ChatMessageRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.chat.ChatMessageResponseDto;
import com.talkjang.demo.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지가 오면 -> db에 저장 후 -> 리턴
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageRequestDto message) {

        CommonResponseDto responseDto = chatMessageService.saveChatMessage(message);

        // 해당 회원한테보내고, 해당 채팅룸에게도 보내고
        messagingTemplate.convertAndSend("/queue/" + message.getToUserId(), responseDto);
        messagingTemplate.convertAndSend("/queue/" + message.getRoomId(), responseDto);
    }
}
