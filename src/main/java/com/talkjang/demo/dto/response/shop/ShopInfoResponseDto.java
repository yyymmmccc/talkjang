package com.talkjang.demo.dto.response.shop;

import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopInfoResponseDto {

    private String userId;
    private String shopName;
    private String profileImageUrl;
    private String introduction;
    private LocalDateTime createdAt;
    private Boolean isShopOwner;
    private Long viewCount;
    private Double avgReviewRating;

    public static ShopInfoResponseDto of(User user, Boolean isShopOwner, Long viewCount, Double avgReviewRating){
        return ShopInfoResponseDto.builder()
                .userId(user.getId())
                .shopName(user.getShopName())
                .profileImageUrl(user.getProfileImageUrl())
                .introduction(user.getIntroduction())
                .createdAt(user.getCreatedAt())
                .isShopOwner(isShopOwner)
                .viewCount(viewCount)
                .avgReviewRating(avgReviewRating)
                .build();
    }

}
