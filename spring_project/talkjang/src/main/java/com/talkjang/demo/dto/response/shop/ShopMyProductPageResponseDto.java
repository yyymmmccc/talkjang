package com.talkjang.demo.dto.response.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopMyProductPageResponseDto {

    private List<ShopMyProductListResponseDto> products;
    private Long pageCount;

    public static ShopMyProductPageResponseDto of(List<ShopMyProductListResponseDto> products, Long pageCount){
        return ShopMyProductPageResponseDto.builder()
                .products(products)
                .pageCount(pageCount)
                .build();
    }
}
