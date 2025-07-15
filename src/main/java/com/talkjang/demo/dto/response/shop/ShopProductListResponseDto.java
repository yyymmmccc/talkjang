package com.talkjang.demo.dto.response.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.talkjang.demo.common.enums.ProductState;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ShopProductListResponseDto {

    private Long id;
    private String productImageUrl;
    private String name;
    private Long price;
    private ProductState state;
    private LocalDateTime createdAt;

    @QueryProjection
    public ShopProductListResponseDto(Long id, String productImageUrl, String name, Long price, ProductState state, LocalDateTime createdAt) {
        this.id = id;
        this.productImageUrl = productImageUrl;
        this.name = name;
        this.price = price;
        this.state = state;
        this.createdAt = createdAt;
    }
}
