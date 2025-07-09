package com.talkjang.demo.dto.response.shop;

import com.talkjang.demo.common.enums.ProductCondition;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.request.product.ProductImageRequestDto;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopMyProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private ProductCondition productCondition;
    private String tradeLocation;
    private String tradeLocationDetail;
    private ProductState state;
    private List<String> productImageUrlList;
    private Long categoryId;

    public static ShopMyProductResponseDto from(Product product){
        return ShopMyProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productCondition(product.getProductCondition())
                .tradeLocation(product.getTradeLocation())
                .tradeLocationDetail(product.getTradeLocationDetail())
                .state(product.getState())
                .productImageUrlList(product.getProductImageList().stream().map(ProductImage::getImageUrl).toList())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
