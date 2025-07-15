package com.talkjang.demo.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHotKeywordListResponseDto {

    private List<ProductListResponseDto> products;
    private String firstSearchKeyword;

    public static ProductHotKeywordListResponseDto of(List<ProductListResponseDto> products, String firstSearchKeyword){
        return ProductHotKeywordListResponseDto.builder()
                .products(products)
                .firstSearchKeyword(firstSearchKeyword)
                .build();
    }
}
