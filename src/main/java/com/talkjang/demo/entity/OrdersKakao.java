package com.talkjang.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersKakao {

    @Id
    @JoinColumn(name = "order_id")
    private Long orderId;

    private String tid;


}
