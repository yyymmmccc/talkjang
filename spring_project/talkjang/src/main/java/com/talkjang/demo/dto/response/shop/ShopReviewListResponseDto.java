package com.talkjang.demo.dto.response.shop;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class ShopReviewListResponseDto {

    // 해당 리뷰아디, 글쓴사람 회원아이디, 글쓴사람 회원 상점이름, 프로필이미지 글받은사람 아이디, 내용, 날짜, 별점평균
    private Long reviewId;
    private String fromUserId;
    private String fromUserShopName;
    private String fromUserProfileImage;
    private Long rating;
    private String content;
    private LocalDateTime createdAt;

    @QueryProjection
    public ShopReviewListResponseDto(Long reviewId, String fromUserId, String fromUserShopName, String fromUserProfileImage, Long rating, String content, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.fromUserId = fromUserId;
        this.fromUserShopName = fromUserShopName;
        this.fromUserProfileImage = fromUserProfileImage;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }
}
