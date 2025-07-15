package com.talkjang.demo.dto.response.order;

import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.entity.Orders;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDto {

    // 주문방식
    // 주문자이름
    // 주문자 전화번호
    // 주문자 주소
    // 주문상품이미지
    // 상품 이름
    // 상품 가격

    // 총 결제가격
    private Long orderId;
    private TradeMethod tradeMethod;
    private String buyerId;
    private String buyerName;
    private String buyerPhone;
    private String buyerDeliveryAddress;
    private String buyerDeliveryAddressDetail;
    private Long buyerPoint;
    private Long productId;
    private String productThumbnailImageUrl;
    private String productName;
    private Long productPrice;
    private Long productDeliveryFee;
    private Long totalAmount;

    public static OrderDetailResponseDto of(Long orderId, TradeMethod tradeMethod, Long totalAmount, Product product, User user){
        return OrderDetailResponseDto.builder()
                .orderId(orderId)
                .tradeMethod(tradeMethod)
                .buyerId(user.getId())
                .buyerName(user.getName())
                .buyerPhone(user.getPhone())
                .buyerDeliveryAddress(user.getAddress())
                .buyerDeliveryAddressDetail(user.getAddressDetail())
                .buyerPoint(user.getPoint())
                .productId(product.getId())
                .productThumbnailImageUrl(product.getProductImageList().get(0).getImageUrl())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .productDeliveryFee(product.getDeliveryFee())
                .totalAmount(totalAmount)
                .build();
    }
}
