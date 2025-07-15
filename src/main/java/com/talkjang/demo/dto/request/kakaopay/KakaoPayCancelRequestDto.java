package com.talkjang.demo.dto.request.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayCancelRequestDto {

    private String cid;
    private String tid;
    private Long cancel_amount;
    private Long cancel_tax_free_amount;

}
