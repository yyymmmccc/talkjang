package com.talkjang.demo.dto.response.order;

import com.talkjang.demo.common.enums.PaymentMethod;
import com.talkjang.demo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderFinalizeResponseDto {

    // 결제수단, 사용자이름, 주문번호, 상품이름, 최종결제금액
    private Long orderId;
    private String buyerId;
    private PaymentMethod paymentMethod;
    private String productName;
    private Long paidAmount;

    public static OrderFinalizeResponseDto from(Orders orders){
        return OrderFinalizeResponseDto.builder()
                .orderId(orders.getId())
                .buyerId(orders.getUser().getId())
                .paymentMethod(orders.getPaymentMethod())
                .productName(orders.getProduct().getName())
                .paidAmount(orders.getPaidAmount())
                .build();
    }
}
