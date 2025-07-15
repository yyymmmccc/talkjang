package com.talkjang.demo.dto.response.favorite;

import com.querydsl.core.annotations.QueryProjection;
import com.talkjang.demo.common.enums.ProductState;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FavoriteProductResponseDto {

    private Long favoriteId;
    private Long productId;
    private String productThumbnailImageUrl;
    private String productName;
    private Long productPrice;
    private ProductState productState;
    private LocalDateTime productCreatedAt;

    @QueryProjection
    public FavoriteProductResponseDto(Long favoriteId, Long productId, String productThumbnailImageUrl, String productName, Long productPrice, ProductState productState, LocalDateTime productCreatedAt) {
        this.favoriteId = favoriteId;
        this.productId = productId;
        this.productThumbnailImageUrl = productThumbnailImageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productState = productState;
        this.productCreatedAt = productCreatedAt;
    }
}
