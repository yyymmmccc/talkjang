package com.talkjang.demo.dto.response.product;

import com.querydsl.core.annotations.QueryProjection;
import com.talkjang.demo.common.enums.ProductCondition;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductDetailResponseDto {

    private Long productId;
    private String name;
    private String description;
    private Long price;
    private Long deliveryFee;
    private ProductCondition productCondition;
    private String tradeLocation;
    private String tradeLocationDetail;
    private ProductState state;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;
    private String sellerId;
    private String shopName;
    private String profileImageUrl;
    private List<String> productImageUrlList;
    private Long favoriteCount;// 좋아요 갯수
    private Boolean isFavoriteOwner; // 내가 좋아요 했는지 여부
    private Boolean isProductOwner; // 내가 올린 상품인지 체크
    private Long viewCount;

    @QueryProjection
    public ProductDetailResponseDto(Long productId, String name, String description, Long price, Long deliveryFee, ProductCondition productCondition, String tradeLocation, String tradeLocationDetail, ProductState state, LocalDateTime createdAt, Long categoryId, String categoryName, String sellerId, String shopName, String profileImageUrl, Long favoriteCount, Boolean isFavoriteOwner, Boolean isProductOwner) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.productCondition = productCondition;
        this.tradeLocation = tradeLocation;
        this.tradeLocationDetail = tradeLocationDetail;
        this.state = state;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.sellerId = sellerId;
        this.shopName = shopName;
        this.profileImageUrl = profileImageUrl;
        this.favoriteCount = favoriteCount;
        this.isFavoriteOwner = isFavoriteOwner;
        this.isProductOwner = isProductOwner;
    }

    public void setProductImageUrlList(List<String> productImageUrlList){
        this.productImageUrlList = productImageUrlList;
    }

    public void setViewCount(Long viewCount){
        this.viewCount = viewCount;
    }
    /*
    public static ProductDetailResponseDto from(Product product){
        return ProductDetailResponseDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productCondition(product.getProductCondition())
                .tradeLocation(product.getTradeLocation())
                .tradeLocationDetail(product.getTradeLocationDetail())
                .state(product.getState())
                .createdAt(product.getCreatedAt())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .shopName(product.getUser().getShopName())
                .productImageUrlList(product.getProductImageList().stream()
                        .map(productImage -> productImage.getImageUrl()).toList())
                .build();
    }

     */

}
