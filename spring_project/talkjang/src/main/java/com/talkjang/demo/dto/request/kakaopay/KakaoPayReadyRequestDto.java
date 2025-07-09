package com.talkjang.demo.dto.request.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayReadyRequestDto {

    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String quantity;
    private String total_amount;
    private String vat_amount;
    private String tax_free_amount;
    private String approval_url;
    private String fail_url;
    private String cancel_url;

}
