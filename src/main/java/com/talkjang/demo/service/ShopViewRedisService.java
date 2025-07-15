package com.talkjang.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ShopViewRedisService {

    private final StringRedisTemplate redisTemplate;

    // jimindong100::SHOP::VIEW_COUNT::지지지온
    private final String SHOP_VIEW_KEY = "::SHOP::VIEW_COUNT::";

    // jimindong100::SHOP::VIEW_LOCK::지지지온
    private final String SHOP_VIEW_LOCK_KEY = "::SHOP_NAME::VIEW_LOCK::";

    private final Duration SHOP_VIEW_LOCK_TIME = Duration.ofMinutes(30);

    public boolean validateShopView(String userId, String shopName){
        // 지지지온::SHOP_NAME::VIEW_LOCK::jimindong100, "", 30분
        return redisTemplate.opsForValue().setIfAbsent(shopName + SHOP_VIEW_LOCK_KEY + userId, "", SHOP_VIEW_LOCK_TIME);
    }

    public Long increaseShopView(String shopName){
        // 지지지온::SHOP::VIEW_COUNT::
        return redisTemplate.opsForValue().increment(shopName + SHOP_VIEW_KEY);
    }

    public Long readShopViewCount(String shopName) {
        String viewCount = redisTemplate.opsForValue().get(shopName + SHOP_VIEW_KEY);
        if(viewCount == null){
            return 0L;
        }
        return Long.valueOf(viewCount);
    }
}
