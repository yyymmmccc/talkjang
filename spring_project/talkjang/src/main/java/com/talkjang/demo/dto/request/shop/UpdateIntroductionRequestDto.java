package com.talkjang.demo.dto.request.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIntroductionRequestDto {

    private String shopName;
    private String introduction;
}
