package com.talkjang.demo.dto.request.order;

import com.talkjang.demo.common.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFinalizeRequestDto {

    private Long usedPoint;
    private Long paidAmount;
    private PaymentMethod paymentMethod;
    private String shippingName;
    private String shippingPhone;
    private String address;
    private String addressDetail;
    private Boolean isAddressSave;
    private String deliveryRequest;

}
