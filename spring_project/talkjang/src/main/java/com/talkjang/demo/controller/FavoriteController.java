package com.talkjang.demo.controller;


import com.talkjang.demo.dto.request.favorite.FavoriteCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 상품 찜하기, 이미 찜 되어있으면 찜 취소
    @PostMapping("")
    public CommonResponseDto toggleFavorite(@AuthenticationPrincipal String userId,
                                            @RequestBody FavoriteCreateRequestDto dto){

        return favoriteService.toggleFavorite(userId, dto);
    }

    @GetMapping("/my")
    public CommonResponseDto getMyFavoriteList(@AuthenticationPrincipal String userId,
                                               @RequestParam(value = "orderBy", defaultValue = "recently") String orderBy,
                                               @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
                                               @RequestParam(value = "lastIndex", required = false) Long lastIndex,
                                               @RequestParam(value = "lastPrice", required = false) Long lastPrice){

        return favoriteService.getMyFavoriteProductList(userId, orderBy, pageSize, lastIndex, lastPrice);
    }

    // 나의 찜한 상품 갯수보기
    @GetMapping("/count")
    public CommonResponseDto countMyFavorite(@AuthenticationPrincipal String userId){

        return CommonResponseDto.success(favoriteService.countMyFavorite(userId));
    }
}
