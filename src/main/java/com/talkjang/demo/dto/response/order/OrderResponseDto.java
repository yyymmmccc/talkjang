package com.talkjang.demo.dto.response.order;

import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.common.enums.PaymentMethod;
import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    // 주문번호, 구매한사람, 판매한사람, 판매상품, 주문일시, 주문요청사항

    private String sellerId;
    private String sellerShopName;
    private Boolean isSellerOwner;
    private Long orderId;
    private LocalDateTime paymentCompletedAt;
    private OrderState orderState;
    private TradeMethod tradeMethod;
    private Long productId;
    private String productThumbnailImageUrl;
    private String productName;
    private Long productPrice;
    private String buyerId;
    private String buyerName;
    private String buyerShopName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerAddressDetail;
    private String deliveryRequest;
    private String courier;
    private String trackingNumber;
    private Long totalAmount;
    private Long usedPoint;
    private Long paidAmount;
    private PaymentMethod paymentMethod;
    private Boolean isBuyerReview;
    private Boolean isSellerReview;

    public static OrderResponseDto of(Orders orders, Boolean isSellerOwner){
        return OrderResponseDto.builder()
                .sellerId(orders.getProduct().getUser().getId())
                .sellerShopName(orders.getProduct().getUser().getShopName())
                .isSellerOwner(isSellerOwner)
                .orderId(orders.getId())
                .paymentCompletedAt(orders.getPaymentCompletedAt())
                .orderState(orders.getState())
                .tradeMethod(orders.getTradeMethod())
                .productId(orders.getProduct().getId())
                .productThumbnailImageUrl(orders.getProduct().getProductImageList().get(0).getImageUrl())
                .productName(orders.getProduct().getName())
                .productPrice(orders.getProduct().getPrice())
                .buyerId(orders.getUser().getId())
                .buyerName(orders.getUser().getName())
                .buyerShopName(orders.getUser().getShopName())
                .buyerPhone(orders.getUser().getPhone())
                .buyerAddress(orders.getShippingAddress())
                .buyerAddressDetail(orders.getShippingAddressDetail())
                .deliveryRequest(orders.getDeliveryRequest())
                .courier(orders.getCourier())
                .trackingNumber(orders.getTrackingNumber())
                .totalAmount(orders.getTotalAmount())
                .usedPoint(orders.getUsedPoint())
                .paidAmount(orders.getPaidAmount())
                .paymentMethod(orders.getPaymentMethod())
                .isBuyerReview(orders.getIsBuyerReview())
                .isSellerReview(orders.getIsSellerReview())
                .build();
    }
}
