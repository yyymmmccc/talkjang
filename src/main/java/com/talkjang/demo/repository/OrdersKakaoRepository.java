package com.talkjang.demo.repository;

import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.OrdersKakao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersKakaoRepository extends JpaRepository<OrdersKakao, Long> {
}
