package com.talkjang.demo.dto.response.order;

import com.querydsl.core.annotations.QueryProjection;
import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.common.enums.TradeMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderSalesResponseDto {

    private Long orderId;
    private LocalDateTime paymentCompletedAt;
    private OrderState orderState;
    private String productThumbnailImageUrl;
    private String productName;
    private Long productPrice;
    private String buyerId;
    private String buyerShopName;
    private TradeMethod tradeMethod;

    @QueryProjection
    public OrderSalesResponseDto(Long orderId, LocalDateTime paymentCompletedAt, OrderState orderState, String productThumbnailImageUrl, String productName, Long productPrice, String buyerId, String buyerShopName, TradeMethod tradeMethod) {
        this.orderId = orderId;
        this.paymentCompletedAt = paymentCompletedAt;
        this.orderState = orderState;
        this.productThumbnailImageUrl = productThumbnailImageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.buyerId = buyerId;
        this.buyerShopName = buyerShopName;
        this.tradeMethod = tradeMethod;
    }
}
