package com.talkjang.demo.dto.request.chat;

import com.talkjang.demo.entity.ChatMessage;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {

    private String roomId;
    private String sender;
    private String content;
    private String toUserId;
    //private MessageType type; // CHAT, JOIN, LEAVE

    public ChatMessage toEntity(User user, ChatRoom chatRoom){
        return ChatMessage.builder()
                .sender(user)
                .chatRoom(chatRoom)
                .content(content)
                .build();
    }
}