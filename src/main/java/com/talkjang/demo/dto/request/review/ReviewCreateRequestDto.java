package com.talkjang.demo.dto.request.review;

import com.talkjang.demo.entity.Review;
import com.talkjang.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequestDto {

    private Long orderId;
    private String content;
    private Long rating;
    private String toUserId;

    public Review toEntity(User fromUser, User toUser){
        return Review.builder()
                .content(content)
                .rating(rating)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }
}
