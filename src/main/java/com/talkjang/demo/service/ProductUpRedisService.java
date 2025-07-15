package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.handler.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUpRedisService {

    private final StringRedisTemplate redisTemplate;
    private Long COOL_DOWN_MILLIS = 60 * 50 * 1000L; // 5분

    private final String PRODUCT_UP_KEY = "PRODUCT::UP::LIMIT::";

    public void checkAndRecordProductUp(String userId){
        Long now = System.currentTimeMillis();
        // 먼저 해당 키에 만료된 애들있는지 확인하고 있으면 삭제
        redisTemplate.opsForZSet().removeRangeByScore(PRODUCT_UP_KEY + userId, 0, now);
        // 그 후 사이즈를 꺼내오기
        Long size = redisTemplate.opsForZSet().size(PRODUCT_UP_KEY + userId);
        // 사이즈가 3이거나크면 오류
        if(size >= 3) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        redisTemplate.opsForZSet().add(PRODUCT_UP_KEY + userId, String.valueOf(now + COOL_DOWN_MILLIS), now + COOL_DOWN_MILLIS);
    }

    public Long countProductUp(String userId){
        // 해당 사이에 값 갯수 구해서 리턴
        Long now = System.currentTimeMillis();
        // 현재시간으로부터 한시간 이내의 데이터만 카운트하기
        return redisTemplate.opsForZSet().count(PRODUCT_UP_KEY + userId, now, now + COOL_DOWN_MILLIS);
    }
}
