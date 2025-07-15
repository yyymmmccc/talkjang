package com.talkjang.demo.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageRequestDto {

    private String productImageUrl;
    private boolean thumbnail;

}
