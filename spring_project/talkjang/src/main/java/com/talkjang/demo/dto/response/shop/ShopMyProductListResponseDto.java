package com.talkjang.demo.dto.response.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.talkjang.demo.common.enums.ProductState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class ShopMyProductListResponseDto {

    private Long productId;
    private String productImageThumbnailUrl;
    private String productName;
    private ProductState productState;
    private Long productPrice;
    private LocalDateTime updatedAt;
    private Long productFavoriteCount;

    @QueryProjection
    public ShopMyProductListResponseDto(Long productId, String productImageThumbnailUrl, String productName, ProductState productState, Long productPrice, LocalDateTime updatedAt, Long productFavoriteCount) {
        this.productId = productId;
        this.productImageThumbnailUrl = productImageThumbnailUrl;
        this.productName = productName;
        this.productState = productState;
        this.productPrice = productPrice;
        this.updatedAt = updatedAt;
        this.productFavoriteCount = productFavoriteCount;
    }
}
