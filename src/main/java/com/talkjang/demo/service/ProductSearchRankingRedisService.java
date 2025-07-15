package com.talkjang.demo.service;

import com.talkjang.demo.dto.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchRankingRedisService {

    private final StringRedisTemplate redisTemplate;
    private final String SEARCH_WORD_RANKING_KEY = "SEARCH_WORD_RANKING";

    // 레디스 값 1증가 시키기
    public void increaseSearchWordScore(String searchWord){

        Double score = redisTemplate.opsForZSet().score(SEARCH_WORD_RANKING_KEY, searchWord);
        if(score == null) score = (double) 0;

        redisTemplate.opsForZSet().add(SEARCH_WORD_RANKING_KEY, searchWord, score + 1);
    }

    public Set<String> getTopSearchWordRankingList(){

        Set<String> rankings = redisTemplate.opsForZSet().reverseRange(SEARCH_WORD_RANKING_KEY, 0, 9);

        return rankings;
    }
}
