package com.talkjang.demo.repository;

import com.talkjang.demo.dto.response.order.OrderPurchasesResponseDto;
import com.talkjang.demo.dto.response.order.OrderSalesResponseDto;

import java.util.List;

public interface OrderCustomRepository {

    List<OrderPurchasesResponseDto> findPurchasesByUserId(String userId, String state, Long lastIndex, Long pageSize);

    List<OrderSalesResponseDto> findSalesByUserId(String userId, String state, Long lastIndex, Long pageSize);
}
