package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.chat.ChatRoomListResponseDto;
import com.talkjang.demo.dto.response.chat.CreateChatRoomResponseDto;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.entity.RoomParticipant;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.RoomParticipantRepository;
import com.talkjang.demo.repository.ChatRoomRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    // 채팅방 생성, 채팅방 삭제 등등

    private final ChatRoomRepository chatRoomRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    private final UserRepository userRepository;

    @Transactional
    public ChatRoom createChatRoom(String fromUserId, String toUserId){
        // 1. 채팅방 둘의 회원정보를 가져옴
        User fromUser = findByUserOrThrow(fromUserId);
        User toUser = findByUserOrThrow(toUserId);
        // 2. 해당 회원아이디 둘을 합쳐서 room_id 를 만듦 (정렬되어있음)
        String roomId = generateRoomId(fromUserId, toUserId);
        // 3. 해당 채팅방이 있는지 조회 후 -> 없으면 해당 room_id 로 채팅방 하나 만듦
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .roomId(roomId)
                                .build()));

        // 4. 채팅방을 만든 후 -> 참여자 목록에 있는지 확인 -> 없다면 해당 채팅방 참여자 목록을 만듦
        saveRoomParticipantIfNotExist(chatRoom, fromUser);
        saveRoomParticipantIfNotExist(chatRoom, toUser);

        // 5. 해당 채팅방번호와, 상대방 상점이름을 리턴
        return chatRoom;
    }

    public CommonResponseDto createChatRoomWithResponse(String fromUserId, String toUserId) {
        ChatRoom chatRoom = createChatRoom(fromUserId, toUserId);
        User toUser = findByUserOrThrow(toUserId);

        return CommonResponseDto.success(
                CreateChatRoomResponseDto.of(chatRoom.getRoomId(), toUser.getShopName())
        );
    }

    // 내가 속한 채팅방 목록 가져오기
    public CommonResponseDto getChatRoomList(String userId) {

        List<ChatRoomListResponseDto> roomParticipants = roomParticipantRepository.findChatRoomsByUserId(userId);

        return CommonResponseDto.success(
                roomParticipants
        );
    }

    private void saveRoomParticipantIfNotExist(ChatRoom chatRoom, User user){
        roomParticipantRepository.findByChatRoomAndUser(chatRoom, user)
                .orElseGet(() -> roomParticipantRepository.save(
                        RoomParticipant.builder()
                                .chatRoom(chatRoom)
                                .user(user)
                                .build()
                ));
    }

    public String generateRoomId(String userA, String userB) {
        List<String> sorted = Arrays.asList(userA, userB);
        Collections.sort(sorted);
        return sorted.get(0) + "_" + sorted.get(1);
    }

    public User findByUserOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

}
