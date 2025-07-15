package com.talkjang.demo.service;

import com.talkjang.demo.common.PagingCalculator;
import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.request.shop.UpdateIntroductionRequestDto;
import com.talkjang.demo.dto.request.shop.UpdateProfileImageRequestDto;
import com.talkjang.demo.dto.request.shop.UpdateShopNameRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.shop.ShopReviewListResponseDto;
import com.talkjang.demo.dto.response.shop.*;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.ProductRepository;
import com.talkjang.demo.repository.ReviewRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ShopViewRedisService shopViewRedisService;
    private final ProductUpRedisService productUpRedisService;

    @Transactional
    public CommonResponseDto getShopInfoByShopName(String userId, String shopName){

        User user = userService.getUserByShopNameOrThrow(shopName);

        if(!userId.equals("anonymousUser") && shopViewRedisService.validateShopView(userId, shopName)){
            // validate -> lock 검사 -> true 면 방문수 1증가
            Long viewCount = shopViewRedisService.increaseShopView(shopName);
            // 방문수 100마다 db에 백업 추가해주기
            if(viewCount % 100 == 0){
                userRepository.shopViewCountBackUp(user.getId(), viewCount);
            }
        }

        Boolean isShopOwner = false;
        if(userId.equals(user.getId())) isShopOwner = true;  // 해당 메서드 요청한 사람과, 해당 shopName 오너가 같은경우 true

        Long viewCount = shopViewRedisService.readShopViewCount(shopName);

        Double avgReview = reviewRepository.averageReviewRatingByUserId(user.getId());

        return CommonResponseDto.success(ShopInfoResponseDto.of(user, isShopOwner, viewCount, avgReview));
    }

    public CommonResponseDto getShopProductsByShopName(String shopName, String orderBy, ProductState state, Long pageSize, Long lastIndex, Long lastPrice) {
        // 해당 사용자의 상품 가져오기
        // 무한 스크롤로 20개씩 가져오기

        boolean hasNext = false;

        List<ShopProductListResponseDto> productsList = productRepository.findShopProductListByShopName(shopName, orderBy, state, pageSize, lastIndex, lastPrice);

        // 상품 전체 갯수 가져오기
        Long productCount = productService.countProductByUser(findUserByShopNameOrThrow(shopName));

        // 빈 배열이면 그냥 리턴
        if(productsList.isEmpty())
            return CommonResponseDto.success(productsList);

        // 무한스크롤 배열사이즈가 > pageSize 보다 큰 경우 다음페이지가 있다는 말이므로 true
        if(productsList.size() > pageSize) hasNext = true;

        // 다음페이지가 있는지 검사 후 last product 를 빼고 새로운 product 리스트 만들기
        List<ShopProductListResponseDto> products = productsList.stream().limit(pageSize).toList();

        return CommonResponseDto.success(
                ShopProductInfinityScrollResponseDto.of(products
                        , productCount
                        , products.getLast().getId()
                        , products.getLast().getPrice()
                        , hasNext)
        );
    }

    // 나의 상품관리에서 보는것들
    public CommonResponseDto getMyProducts(String userId, Long page, Long pageSize, ProductState state, String searchWord) {

        // 해당 상품 갯수 (state 상태에 따라 다름)
        Long pageCount = productRepository.countMyProducts(userId, state, PagingCalculator.pageCountCalc(page - 1, pageSize), searchWord);

        return CommonResponseDto.success(
                ShopMyProductPageResponseDto.of(
                        productRepository.findMyProducts(userId, (page - 1) * 10, pageSize, state, searchWord),
                        pageCount
                )
        );
    }

    public CommonResponseDto getMyProduct(String userId, Long productId) {
        // 상품을 가져옴 -> userId랑 상품 등록한 사람 다르면 forbidden
        // 상품을 가져옴 -> userId랑 같다면 -> responseDto 리턴
        Product product = productRepository.findProductFetchJoinProductImageById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        // 상품등록한 사람과 나의상품 가져오려는 사람이 다른경우
        if(!product.getUser().getId().equals(userId))
            throw new CustomException(ErrorCode.FORBIDDEN);

        return CommonResponseDto.success(ShopMyProductResponseDto.from(product));
    }

    public CommonResponseDto getReviews(String shopName, Long lastIndex, Long pageSize){

        Boolean hasNext = false;

        User user = findUserByShopNameOrThrow(shopName);

        List<ShopReviewListResponseDto> reviewList = reviewRepository.findAllByToUserId(user.getId(), lastIndex, pageSize);

        Long count = reviewRepository.countByToUserId(user.getId());

        // 쿼리에서 limit 5개를 가져옴, pageSize 4보다 크면 다음페이지가 있으므로 hasNext = true
        if(reviewList.size() > pageSize) hasNext = true;

        List<ShopReviewListResponseDto> reviews = reviewList.stream().limit(pageSize).toList();

        return CommonResponseDto.success(
                ShopReviewInfinityScrollResponseDto.of(reviews, count, reviews.getLast().getReviewId(), hasNext)
        );
    }

    @Transactional
    public CommonResponseDto myProductUpIncrement(String userId, Long productId) {
        // redis 에서 해당 사용자 up 남았는지 체크
        // up이 남았으면 productId updatedAt 변경
        // up이 안남았으면 throw up 횟수가 부족합니다.

        productUpRedisService.checkAndRecordProductUp(userId);
        productRepository.upAtByProductId(productId, LocalDateTime.now());
        return CommonResponseDto.success(null);
    }

    public CommonResponseDto myProductUpCount(String userId){
        return CommonResponseDto.success(productUpRedisService.countProductUp(userId));
    }

    @Transactional
    public CommonResponseDto updateProfileImage(String userId, UpdateProfileImageRequestDto dto) {
        String newProfileImageUrl = userService.updateProfileImage(userId, dto.getProfileImageUrl());

        return CommonResponseDto.success(newProfileImageUrl);
    }

    @Transactional
    public CommonResponseDto updateShopName(UpdateShopNameRequestDto dto){
        User user = userService.updateShopName(dto.getOldShopName(), dto.getNewShopName());

        return CommonResponseDto.success(user.getShopName());
    }

    @Transactional
    public CommonResponseDto updateIntroduction(UpdateIntroductionRequestDto dto) {
        User user = userService.updateIntroduction(dto);

        return CommonResponseDto.success(user.getIntroduction());
    }

    public User findUserByShopNameOrThrow(String shopName) {
        return userRepository.findByShopName(shopName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
