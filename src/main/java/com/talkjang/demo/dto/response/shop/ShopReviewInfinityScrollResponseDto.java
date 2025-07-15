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
public class ShopReviewInfinityScrollResponseDto {

    private List<ShopReviewListResponseDto> reviews;
    private Long reviewCount;
    private Long lastIndex;
    private Boolean hasNext;

    public static ShopReviewInfinityScrollResponseDto of(List<ShopReviewListResponseDto> reviews, Long reviewCount, Long lastIndex, Boolean hasNext){
        return ShopReviewInfinityScrollResponseDto.builder()
                .reviews(reviews)
                .reviewCount(reviewCount)
                .lastIndex(lastIndex)
                .hasNext(hasNext)
                .build();
    }
}
