package com.talkjang.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ProductViewRedisService {

    private final StringRedisTemplate redisTemplate;

    // 상품번호::PRODUCT::VIEW_COUNT : 623 봄
    private final String PRODUCT_VIEW_KEY = "::PRODUCT::VIEW_COUNT";

    // 상품번호::PRODUCT::VIEW_LOCK::jimindong100
    private final String PRODUCT_VIEW_LOCK_KEY = "::PRODUCT::VIEW_LOCK::";
    private final Duration PRODUCT_VIEW_LOCK_TIME = Duration.ofMinutes(30);

    // 락걸기, 상품 방문자 1증가, 해당 상품 방문수 가져오기
    public Boolean validateProductView(String userId, Long productId){
        // 어떤사용자가 해당 상품을 본 경우, lock_time 내에는 조회수 증가 x
        return redisTemplate.opsForValue().setIfAbsent(productId + PRODUCT_VIEW_LOCK_KEY + userId, "", PRODUCT_VIEW_LOCK_TIME);
    }

    public Long increaseProductView(Long productId){
        // 해당 상품 조회수 1증가 시키기
        return redisTemplate.opsForValue().increment(productId + PRODUCT_VIEW_KEY);
    }

    public Long readProductViewCount(Long productId){
        String viewCount = redisTemplate.opsForValue().get(productId + PRODUCT_VIEW_KEY);
        if(viewCount == null){
            return 0L;
        }
        return Long.valueOf(viewCount);

    }
}
