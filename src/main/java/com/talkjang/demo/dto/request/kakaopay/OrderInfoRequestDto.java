package com.talkjang.demo.dto.request.kakaopay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderInfoRequestDto {

    private String partner_order_id;
    private String item_name;
    private String quantity;
    private String total_amount;
    private String vat_amount;
    private String tax_free_amount;
}
