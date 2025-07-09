package com.talkjang.demo.dto.response.chat;

import com.talkjang.demo.entity.RoomParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomListResponseDto {

    private String roomId;
    private String opponentUserId;
    private String opponentShopName;
    private String opponentProfileImageUrl;
    private String lastContent;
    private LocalDateTime lastCreatedAt;
    private Long unreadMessageCount;
    // 마지막 채팅내용뭔지
    // 마지막 채팅날짜언제인지
    // 누구랑 얘기하는지
    // 누구의 프사와 닉네임가져오기

    // **** 마지막 채팅 몇개인지 검사하기

    public ChatRoomListResponseDto(String roomId, String opponentUserId, String opponentShopName, String opponentProfileImageUrl, String lastContent, Timestamp lastCreatedAt, Long unreadMessageCount) {
        this.roomId = roomId;
        this.opponentUserId = opponentUserId;
        this.opponentShopName = opponentShopName;
        this.opponentProfileImageUrl = opponentProfileImageUrl;
        this.lastContent = lastContent;
        this.lastCreatedAt = lastCreatedAt != null ? lastCreatedAt.toLocalDateTime() : null;
        this.unreadMessageCount = unreadMessageCount;
    }

}
