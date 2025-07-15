package com.talkjang.demo.repository;

import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.response.favorite.FavoriteProductResponseDto;
import com.talkjang.demo.dto.response.product.ProductDetailResponseDto;
import com.talkjang.demo.dto.response.product.ProductListResponseDto;
import com.talkjang.demo.dto.response.shop.ShopMyProductListResponseDto;
import com.talkjang.demo.dto.response.shop.ShopProductListResponseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCustomRepository {

    List<ProductListResponseDto> findAllByCategoryAndImageThumbnail(Long categoryId, Long offset, Long limit, String orderBy, String searchWord);

    List<ShopProductListResponseDto> findShopProductListByShopName(String shopName, String orderBy, ProductState state, Long pageSize, Long lastIndex, Long lastPrice);

    ProductDetailResponseDto findProductDetailsWithFavoriteCountAndFavoriteStatus(String userId, Long productId);

    List<FavoriteProductResponseDto> findFavoriteProductsByUserId(String userId, String orderBy, Long pageSize, Long lastIndex, Long lastPrice);

    List<ShopMyProductListResponseDto> findMyProducts(String userId, Long page, Long pageSize, ProductState state, String searchWord);

    List<ProductListResponseDto> getTopFavoriteProductList(@Param("productState") ProductState productState);

    Long countMyProducts(String userId, ProductState state, Long limit, String searchWord);

    List<ProductListResponseDto> getTopViewProductList();

    List<ProductListResponseDto> getTopKeywordProductList(String firstKeyword);
}
