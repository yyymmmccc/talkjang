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
public class ShopProductInfinityScrollResponseDto {

    private List<ShopProductListResponseDto> products;
    private Long productCount;
    private Long lastIndex;
    private Long lastPrice;
    private boolean hasNext;

    public static ShopProductInfinityScrollResponseDto of(List<ShopProductListResponseDto> products, Long productCount, Long lastIndex, Long lastPrice, boolean hasNext){
        return ShopProductInfinityScrollResponseDto.builder()
                .products(products)
                .productCount(productCount)
                .lastIndex(lastIndex)
                .lastPrice(lastPrice)
                .hasNext(hasNext)
                .build();
    }
}
