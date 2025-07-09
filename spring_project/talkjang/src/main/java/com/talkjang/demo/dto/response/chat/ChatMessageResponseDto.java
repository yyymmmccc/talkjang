package com.talkjang.demo.dto.response.chat;

import com.talkjang.demo.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDto {

    private String content;
    private String senderUserId;
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto from(ChatMessage chatMessage){
        return ChatMessageResponseDto.builder()
                .content(chatMessage.getContent())
                .senderUserId(chatMessage.getSender().getId())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
