package com.talkjang.demo.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateTrackingNumberRequestDto {

    private String courier;
    private String trackingNumber;
}
