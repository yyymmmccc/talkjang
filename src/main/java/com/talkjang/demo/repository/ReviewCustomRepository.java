package com.talkjang.demo.repository;

import com.talkjang.demo.dto.response.shop.ShopReviewListResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCustomRepository {

    List<ShopReviewListResponseDto> findAllByToUserId(String userId, Long lastIndex, Long pageSize);
}
