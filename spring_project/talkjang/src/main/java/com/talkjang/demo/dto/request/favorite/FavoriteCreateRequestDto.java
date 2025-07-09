package com.talkjang.demo.dto.request.favorite;

import com.talkjang.demo.entity.Favorite;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCreateRequestDto {

    private Long productId;

    public static Favorite toEntity(User user, Product product){
        return Favorite.builder()
                .product(product)
                .user(user)
                .build();
    }
}
