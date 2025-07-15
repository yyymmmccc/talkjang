package com.talkjang.demo.dto.cache.order;

import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.common.enums.PaymentMethod;
import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRedisDto {

    private Long orderId;
    private Long productId;
    private String buyerId;
    private TradeMethod tradeMethod;
    private PaymentMethod paymentMethod;
    private Long totalAmount;
    private Long usedPoint;
    private Long paidAmount;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String shippingAddressDetail;
    private String deliveryRequest;

    public Orders toEntity(Product product, User user){
        return Orders.builder()
                .id(orderId)
                .product(product)
                .user(user)
                .state(OrderState.PAID)
                .tradeMethod(tradeMethod)
                .totalAmount(totalAmount)
                .usedPoint(usedPoint)
                .paidAmount(paidAmount)
                .paymentMethod(paymentMethod)
                .shippingName(shippingName)
                .shippingAddress(shippingAddress)
                .shippingAddressDetail(shippingAddressDetail)
                .deliveryRequest(deliveryRequest)
                .isBuyerReview(false)
                .isSellerReview(false)
                .build();
    }

}
