package com.talkjang.demo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.talkjang.demo.dto.response.shop.QShopReviewListResponseDto;
import com.talkjang.demo.dto.response.shop.ShopReviewListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.talkjang.demo.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public List<ShopReviewListResponseDto> findAllByToUserId(String toUserId, Long lastIndex, Long pageSize){
        return jpaQueryFactory.select(
                new QShopReviewListResponseDto(
                        review.id,
                        review.fromUser.id,
                        review.fromUser.shopName,
                        review.fromUser.profileImageUrl,
                        review.rating,
                        review.content,
                        review.createdAt
                ))
                .from(review)
                .where(review.toUser.id.eq(toUserId)
                        .and(lastIndex != null ? review.id.lt(lastIndex) : null))
                .limit(pageSize + 1)
                .orderBy(review.id.desc())
                .fetch();
    }
}
