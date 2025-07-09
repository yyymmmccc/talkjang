package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.request.favorite.FavoriteCreateRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.favorite.FavoriteProductInfinityScrollResponseDto;
import com.talkjang.demo.dto.response.favorite.FavoriteProductResponseDto;
import com.talkjang.demo.entity.Favorite;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.FavoriteRepository;
import com.talkjang.demo.repository.ProductRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 찜 안되어있으면 찜, 되어있으면 찜 취소
    @Transactional
    public CommonResponseDto toggleFavorite(String userId, FavoriteCreateRequestDto dto){
        User user = getUserByUserIdOrThrow(userId);
        Product product = getProductByIdOrThrow(dto.getProductId());

        if(product.getUser().equals(user)){ // 자기자신의 상품에 찜을하는경우
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product);

        if(favorite == null){ // 찜을 안누른경우
            favoriteRepository.save(dto.toEntity(user, product));
        }
        else{ // 찜을 누른경우일 때 -> 다시 삭제
            favoriteRepository.delete(favorite);
        }

        Long favoriteCount = favoriteRepository.countByProductId(product.getId());

        return CommonResponseDto.success(favoriteCount);
    }

    // 해당 사용자가 찜한 상품 목록 보여주기
    public CommonResponseDto getMyFavoriteProductList(String userId, String orderBy, Long pageSize, Long lastIndex, Long lastPrice) {

        boolean hasNext = false;

        List<FavoriteProductResponseDto> myFavoriteProducts = productRepository.findFavoriteProductsByUserId(userId, orderBy, pageSize, lastIndex, lastPrice);

        // 상품이 하나도 없을 때는 그냥 리턴
        if(myFavoriteProducts.isEmpty())
            return CommonResponseDto.success(null);

        if(myFavoriteProducts.size() > pageSize) hasNext = true; // pageSize 보다 큰경우 다음페이지 존재

        Long favoriteProductCount = countMyFavorite(userId);

        List<FavoriteProductResponseDto> responseDto =
                myFavoriteProducts.stream()
                .limit(pageSize)
                .toList();

        return CommonResponseDto.success(
                FavoriteProductInfinityScrollResponseDto.of(
                        responseDto,
                        favoriteProductCount,
                        hasNext,
                        responseDto.getLast().getFavoriteId(),
                        responseDto.getLast().getProductPrice()
                )
        );
    }

    public Long countMyFavorite(String userId) {

        return favoriteRepository.countByUserId(userId);
    }

    public User getUserByUserIdOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public Product getProductByIdOrThrow(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }
}
