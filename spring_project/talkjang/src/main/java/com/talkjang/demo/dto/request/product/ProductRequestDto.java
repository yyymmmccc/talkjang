package com.talkjang.demo.dto.request.product;

import com.talkjang.demo.common.enums.ProductCondition;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.entity.Category;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String name;
    private String description;
    private Long price;
    private Long deliveryFee;
    private ProductCondition productCondition;
    private String tradeLocation;
    private String tradeLocationDetail;
    private ProductState state;
    private List<ProductImageRequestDto> productImageRequestDtoList;
    private Long categoryId;

    public Product toEntity(User user, Category category){
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .deliveryFee(deliveryFee)
                .productCondition(productCondition)
                .tradeLocation(tradeLocation)
                .tradeLocationDetail(tradeLocationDetail)
                .state(state)
                .viewCount(0L)
                .user(user)
                .category(category)
                .build();
    }

}
