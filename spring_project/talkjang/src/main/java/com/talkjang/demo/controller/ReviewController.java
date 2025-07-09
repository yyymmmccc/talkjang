package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.review.ReviewCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public CommonResponseDto create(@AuthenticationPrincipal String userId,
                                    @RequestBody ReviewCreateRequestDto dto){

        return reviewService.create(userId, dto);
    }
}
