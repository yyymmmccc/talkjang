package com.talkjang.demo.repository;

import com.talkjang.demo.entity.ChatMessage;
import com.talkjang.demo.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(
            value = "SELECT * FROM chat_message" +
                    "   WHERE room_id = :roomId" +
                    "   AND (receiver_id IS NULL OR receiver_id = :userId)",
            nativeQuery = true
    )
    List<ChatMessage> findAllByChatRoom(@Param("roomId") String roomId, @Param("userId") String userId);
}
