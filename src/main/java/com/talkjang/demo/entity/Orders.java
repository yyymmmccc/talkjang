package com.talkjang.demo.entity;

import com.talkjang.demo.common.enums.OrderState;
import com.talkjang.demo.common.enums.PaymentMethod;
import com.talkjang.demo.common.enums.TradeMethod;
import com.talkjang.demo.dto.request.order.OrderFinalizeRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Orders {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(name = "trade_method")
    @Enumerated(EnumType.STRING)
    private TradeMethod tradeMethod;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Column(name = "used_point")
    private Long usedPoint;

    @Column(name = "paid_amount")
    private Long paidAmount;

    // 결제방식(카카오, 토스, 카드결제 등)
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "shipping_name")
    private String shippingName;

    @Column(name = "shipping_phone")
    private String shippingPhone;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_address_detail")
    private String shippingAddressDetail;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    private String courier;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "payment_completed_at")
    private LocalDateTime paymentCompletedAt;

    @Column(name = "tracking_number_registered_at")
    private LocalDateTime trackingNumberRegisteredAt;

    @Column(name = "buyer_review")
    private Boolean isBuyerReview;

    @Column(name = "seller_review")
    private Boolean isSellerReview;

    public void updateOrderState(OrderState orderState){
        this.state = orderState;
    }

    public void updateTrackingNumberRegisteredAt(LocalDateTime now){
        this.trackingNumberRegisteredAt = now;
    }

    public void updateCourierAndTrackingNumber(String courier, String trackingNumber){
        this.courier = courier;
        this.trackingNumber = trackingNumber;
    }

    public void updateSellerReview(){
        this.isSellerReview = true;
    }

    public void updateBuyerReview(){
        this.isBuyerReview = true;
    }
}
