package com.talkjang.demo.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChatRoomResponseDto {

    private String roomId;
    private String shopName;

    public static CreateChatRoomResponseDto of(String roomId, String shopName){
        return CreateChatRoomResponseDto.builder()
                .roomId(roomId)
                .shopName(shopName)
                .build();
    }
}
