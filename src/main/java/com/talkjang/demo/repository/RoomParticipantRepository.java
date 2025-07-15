package com.talkjang.demo.repository;

import com.talkjang.demo.dto.response.chat.ChatRoomListResponseDto;
import com.talkjang.demo.entity.ChatRoom;
import com.talkjang.demo.entity.RoomParticipant;
import com.talkjang.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {


    Optional<RoomParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    @Query(
            value = "SELECT cr.room_id, u.id, u.shop_name, u.profile_image_url, cm.content, cm.created_at" +
                    ", (SELECT COUNT(*) FROM chat_message " +
                    "       WHERE room_id = cr.room_id " +
                    "       AND rp_me.last_read_message_at < created_at" +
                    "       AND sender_id != :userId" +
                    "       AND (receiver_id IS NULL OR receiver_id = :userId)" +
                    "   ) AS unReadMessageCount" +
                    "   FROM room_participant AS rp_me" +
                    "   JOIN chat_room AS cr" +
                    "   ON cr.room_id = rp_me.room_id" +
                    "   JOIN room_participant AS rp_opponent" +
                    "   ON cr.room_id = rp_opponent.room_id AND rp_opponent.user_id != :userId" +
                    "   JOIN user as u" +
                    "   ON u.id = rp_opponent.user_id" +
                    "   LEFT JOIN chat_message AS cm" +
                    "   ON cr.room_id = cm.room_id" +
                    "   AND cm.id = (" +
                    "       SELECT id FROM chat_message" +
                    "       WHERE cr.room_id = room_id" +
                    "       AND (receiver_id IS NULL OR receiver_id = :userId)" +
                    "       ORDER BY created_at DESC" +
                    "       LIMIT 1)" +
                    "   WHERE rp_me.user_id = :userId" +
                    "   ORDER BY cm.created_at DESC",
            nativeQuery = true
    )
    List<ChatRoomListResponseDto> findChatRoomsByUserId(@Param("userId") String userId);

    @Modifying
    @Query(
            value = "UPDATE room_participant AS rp" +
                    "   SET rp.last_read_message_at = :now" +
                    "   WHERE rp.room_id = :roomId" +
                    "   AND rp.user_id = :userId",
            nativeQuery = true
    )
    void updateLastReadMessageAt(@Param("userId") String userId, @Param("roomId") String roomId, @Param("now") LocalDateTime now);
}
