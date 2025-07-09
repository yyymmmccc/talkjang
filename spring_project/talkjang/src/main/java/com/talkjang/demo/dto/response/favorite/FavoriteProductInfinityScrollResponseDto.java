package com.talkjang.demo.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteProductInfinityScrollResponseDto {

    private List<FavoriteProductResponseDto> products;
    private Long favoriteProductCount;
    private Boolean hasNext;
    private Long lastIndex;
    private Long lastPrice;

    public static FavoriteProductInfinityScrollResponseDto of(List<FavoriteProductResponseDto> products, Long favoriteProductCount, Boolean hasNext, Long lastIndex, Long lastPrice){
        return FavoriteProductInfinityScrollResponseDto.builder()
                .products(products)
                .favoriteProductCount(favoriteProductCount)
                .hasNext(hasNext)
                .lastIndex(lastIndex)
                .lastPrice(lastPrice)
                .build();
    }
}
