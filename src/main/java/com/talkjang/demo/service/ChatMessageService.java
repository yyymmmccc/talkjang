package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.chat.ChatMessageRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.chat.ChatMessageResponseDto;
import com.talkjang.demo.entity.ChatMessage;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.entity.RoomParticipant;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.ChatMessageRepository;
import com.talkjang.demo.repository.ChatRoomRepository;
import com.talkjang.demo.repository.RoomParticipantRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 메시지 생성, 조회 등등

    @Transactional
    public CommonResponseDto saveChatMessage(ChatMessageRequestDto dto){

        User user = userRepository.findById(dto.getSender())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        ChatRoom chatRoom = findByChatRoomOrThrow(dto.getRoomId());

        ChatMessage chatMessage = chatMessageRepository.save(dto.toEntity(user, chatRoom));

        return CommonResponseDto.success(ChatMessageResponseDto.from(chatMessage));
    }


    // room_id 해당하는 메시지들 다 가져오기 -> 그리고 roomParticipant(채팅목록)에 마지막 읽은 날짜 수정
    @Transactional
    public CommonResponseDto getChatMessagesByRoomId(String userId, String roomId) {

        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoom(roomId, userId);

        // 해당 방에 해당 회원이 마지막으로 채팅 본 시간 업데이트
        updateLastReadMessageAt(userId, roomId);

        return CommonResponseDto.success(
                chatMessageList.stream()
                        .map(ChatMessageResponseDto::from)
                        .toList()
        );
    }

    @Transactional
    public void updateLastReadMessageAt(String userId, String roomId){
        roomParticipantRepository.updateLastReadMessageAt(userId, roomId, LocalDateTime.now());
    }

    public ChatRoom findByChatRoomOrThrow(String roomId){
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT_ROOM));
    }
}
