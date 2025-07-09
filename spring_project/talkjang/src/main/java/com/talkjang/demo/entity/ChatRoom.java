package com.talkjang.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatRoom {

    @Id
    @Column(name = "room_id")
    private String roomId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList = new ArrayList<>();
}
