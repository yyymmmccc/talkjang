package com.talkjang.demo.dto.response.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@Slf4j
public class ProductListResponseDto {

    private Long id;
    private String name;
    private Long price;
    private LocalDateTime createdAt;
    private String productThumbnailImageUrl;

    @QueryProjection
    public ProductListResponseDto(Long id, String name, Long price, LocalDateTime createdAt, String productThumbnailImageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.productThumbnailImageUrl = productThumbnailImageUrl;
    }
}
