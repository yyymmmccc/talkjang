package com.talkjang.demo.controller;

import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.request.shop.UpdateIntroductionRequestDto;
import com.talkjang.demo.dto.request.shop.UpdateProfileImageRequestDto;
import com.talkjang.demo.dto.request.shop.UpdateShopNameRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.ShopService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ShopService shopService;

    // 사용자 이름으로 해당 상점 프로필 가져오기 (상점이름, 평점, 방문일수,
    @GetMapping("/{shopName}")
    public CommonResponseDto getShopInfoByShopName(@AuthenticationPrincipal String userId,
                                                   @PathVariable("shopName") String shopName){

        return shopService.getShopInfoByShopName(userId, shopName);
    }

    @GetMapping("/{shopName}/products")
    public CommonResponseDto getShopProductsByShopName(@PathVariable("shopName") String shopName,
                                                       @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy,
                                                       @RequestParam(value = "state", defaultValue = "ALL") ProductState state,
                                                       @RequestParam(value = "pageSize", defaultValue = "20") Long pageSize,
                                                       @RequestParam(value = "lastIndex", required = false) Long lastIndex,
                                                       @RequestParam(value = "lastPrice", required = false) Long lastPrice){

        // lastIndex 요청이 없으면, 첫번째 페이지.
        return shopService.getShopProductsByShopName(shopName, orderBy, state, pageSize, lastIndex, lastPrice);
    }

    // 상품관리 페이지 -> 내가 올린 상품들 가져오기
    @GetMapping("/my/products")
    public CommonResponseDto getMyProducts(@AuthenticationPrincipal String userId,
                                           @RequestParam(value = "page", defaultValue = "1") Long page,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
                                           @RequestParam(value = "state", defaultValue = "ALL") ProductState state,
                                           @RequestParam(value = "searchWord", defaultValue = "") String searchWord){

        return shopService.getMyProducts(userId, page, pageSize, state, searchWord);
    }

    // 상품관리.html -> 해당상품 수정하기 -> 해당 상품정보 가져올 떄 사용하는 엔드포인트
    @GetMapping("/{productId}/product")
    public CommonResponseDto getMyProduct(@AuthenticationPrincipal String userId,
                                          @PathVariable("productId") Long productId){

        return shopService.getMyProduct(userId, productId);
    }

    // 해당 상점 리뷰 리스트 가져오기
    @GetMapping("/{shopName}/reviews")
    public CommonResponseDto getReviews(@PathVariable("shopName") String shopName,
                                        @RequestParam(name = "lastIndex", required = false) Long lastIndex,
                                        @RequestParam(name = "pageSize", defaultValue = "4") Long pageSize){

        return shopService.getReviews(shopName, lastIndex, pageSize);
    }

    // 해당 상품 up (up을 누를 시 예전상품도 맨위로가게함)
    @PostMapping("/{productId}/up")
    public CommonResponseDto myProductUpIncrement(@AuthenticationPrincipal String userId,
                                         @PathVariable("productId") Long productId){

        return shopService.myProductUpIncrement(userId, productId);
    }

    // 현재 남은 up 갯수 가져오기
    @GetMapping("/up-count")
    public CommonResponseDto myProductUpCount(@AuthenticationPrincipal String userId){

        return shopService.myProductUpCount(userId);
    }

    // 상점 나의 프로필이미지 변경하기
    @PatchMapping("/profile-image/update")
    public CommonResponseDto updateProfileImage(@AuthenticationPrincipal String userId,
                                                @RequestBody UpdateProfileImageRequestDto dto){

        return shopService.updateProfileImage(userId, dto);
    }

    @PatchMapping("/shop-name/update")
    public CommonResponseDto updateShopName(@RequestBody UpdateShopNameRequestDto dto){

        return shopService.updateShopName(dto);
    }

    @PatchMapping("/shop-introduction/update")
    public CommonResponseDto updateIntroduction(@RequestBody UpdateIntroductionRequestDto dto){

        return shopService.updateIntroduction(dto);
    }

}
