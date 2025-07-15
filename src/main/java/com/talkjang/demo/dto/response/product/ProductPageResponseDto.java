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
public class ProductPageResponseDto {

    private List<ProductListResponseDto> productList;
    private Long pageCount;

    public static ProductPageResponseDto of(List<ProductListResponseDto> productList, Long pageCount){
        return ProductPageResponseDto.builder()
                .productList(productList)
                .pageCount(pageCount)
                .build();
    }
}
