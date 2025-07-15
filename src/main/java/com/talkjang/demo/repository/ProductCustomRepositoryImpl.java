package com.talkjang.demo.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.response.favorite.FavoriteProductResponseDto;
import com.talkjang.demo.dto.response.favorite.QFavoriteProductResponseDto;
import com.talkjang.demo.dto.response.product.ProductDetailResponseDto;
import com.talkjang.demo.dto.response.product.ProductListResponseDto;
import com.talkjang.demo.dto.response.product.QProductDetailResponseDto;
import com.talkjang.demo.dto.response.product.QProductListResponseDto;
import com.talkjang.demo.dto.response.shop.QShopMyProductListResponseDto;
import com.talkjang.demo.dto.response.shop.ShopMyProductListResponseDto;
import com.talkjang.demo.dto.response.shop.ShopProductListResponseDto;
import com.talkjang.demo.dto.response.shop.QShopProductListResponseDto;
import com.talkjang.demo.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.talkjang.demo.entity.QCategory.category;
import static com.talkjang.demo.entity.QFavorite.favorite;
import static com.talkjang.demo.entity.QProduct.product;
import static com.talkjang.demo.entity.QProductImage.productImage;
import static com.talkjang.demo.entity.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductCustomRepositoryImpl implements ProductCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    // 상품 상세보기
    public ProductDetailResponseDto findProductDetailsWithFavoriteCountAndFavoriteStatus(String userId, Long productId){

        ProductDetailResponseDto responseDto =  jpaQueryFactory
                .select(new QProductDetailResponseDto(
                        product.id,
                        product.name,
                        product.description,
                        product.price,
                        product.deliveryFee,
                        product.productCondition,
                        product.tradeLocation,
                        product.tradeLocationDetail,
                        product.state,
                        product.createdAt,
                        product.category.id,
                        product.category.name,
                        product.user.id,
                        product.user.shopName,
                        product.user.profileImageUrl,
                        favorite.id.count(),
                        JPAExpressions.selectOne() // 서브쿼리로 내가 좋아요 눌렀는지 체크
                                .from(favorite)
                                .where(favorite.product.id.eq(product.id)
                                        .and(favorite.user.id.eq(userId)))
                                .exists(),
                        JPAExpressions.selectOne()
                                .from(user)  // 조회된 상품에 판매자와, userId 비교, 맞으면 실제들어있는 user 테이블에서 검사실행
                                .where(product.user.id.eq(userId))
                                .exists()
                ))
                .from(product)
                .join(product.category, category) // on을 안써도 자동으로 product.category.id.eq(category.id)
                .join(product.user, user)
                .leftJoin(favorite).on(product.id.eq(favorite.product.id))
                .where(product.id.eq(productId))
                .fetchOne();

        List<ProductImage> productImageList = jpaQueryFactory
                .selectFrom(productImage)
                .where(productImage.product.id.eq(productId))
                .fetch();

        // 한번에 쿼리로 상품에 여러 이미지들을 가져올 수 없으므로 -> 상품 먼저 조회, 그 다음 상품에 이미지 조회해서 해당 객체에 추가
        responseDto.setProductImageUrlList(
                productImageList.stream()
                .map(ProductImage::getImageUrl).toList()
        );

        return responseDto;
    }

    // 해당 카테고리 상품 리스트
    public List<ProductListResponseDto> findAllByCategoryAndImageThumbnail(Long categoryId, Long offset, Long limit, String orderBy, String searchWord){

        return jpaQueryFactory
                .select(new QProductListResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.createdAt,
                        productImage.imageUrl
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .where(productCategory(categoryId))
                .where(productSearchWord(searchWord))
                .where(product.state.eq(ProductState.ON_SALE))
                .orderBy(productOrderSpecifier(orderBy))
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    // 찜 누른 상품 가져오기
    public List<FavoriteProductResponseDto> findFavoriteProductsByUserId(String userId, String orderBy, Long pageSize, Long lastIndex, Long lastPrice) {
        return jpaQueryFactory
                .select(new QFavoriteProductResponseDto(
                        favorite.id,
                        product.id,
                        productImage.imageUrl,
                        product.name,
                        product.price,
                        product.state,
                        product.createdAt
                ))
                .from(favorite)
                .join(product)
                .on(product.id.eq(favorite.product.id))
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .where(favorite.user.id.eq(userId).and(favoritePaginationCursorFilter(orderBy, lastIndex, lastPrice)))
                .groupBy(favorite.id,
                        product.id,
                        productImage.imageUrl,
                        product.name,
                        product.price,
                        product.state,
                        product.createdAt)
                .orderBy(favoriteProductOrderSpecifier(orderBy))
                .limit(pageSize + 1) // 다음 페이지가 있는지 확인하기 위해 -> 보여질 상품보다 하나 더 가져옴
                .fetch();
    }

    // 상점이름에 해당하는 상품들 가져오기
    public List<ShopProductListResponseDto> findShopProductListByShopName(String shopName,
                                                                          String orderBy,
                                                                          ProductState state,
                                                                          Long pageSize,
                                                                          Long lastIndex,
                                                                          Long lastPrice){
        return jpaQueryFactory
                .select(new QShopProductListResponseDto(
                        product.id,
                        productImage.imageUrl,
                        product.name,
                        product.price,
                        product.state,
                        product.createdAt
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id)
                        .and(productImage.thumbnail.eq(true)))
                .where(
                        product.user.shopName.eq(shopName)
                                .and(productState(state))
                                .and(productPaginationCursorFilter(orderBy, lastIndex, lastPrice)))
                .orderBy(productOrderSpecifier(orderBy))
                .limit(pageSize + 1)
                .fetch();
    }

    public List<ShopMyProductListResponseDto> findMyProducts(String userId, Long page, Long pageSize, ProductState state, String searchWord){
        return jpaQueryFactory
                .select(new QShopMyProductListResponseDto(
                        product.id,
                        productImage.imageUrl,
                        product.name,
                        product.state,
                        product.price,
                        product.updatedAt,
                        favorite.id.count()
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .leftJoin(favorite)
                .on(product.id.eq(favorite.product.id))
                .where(product.user.id.eq(userId)
                        .and(state == ProductState.ALL ? null : product.state.eq(state))
                        .and(searchWord.isEmpty() ? null : product.name.containsIgnoreCase(searchWord))
                )
                .groupBy(
                        product.id,
                        productImage.imageUrl,
                        product.name,
                        product.state,
                        product.price,
                        product.updatedAt
                )
                .orderBy(product.upAt.desc(), product.updatedAt.desc())
                .limit(pageSize)
                .offset(page)
                .fetch();
    }

    public List<ProductListResponseDto> getTopFavoriteProductList(ProductState productState){
        return jpaQueryFactory
                .select(new QProductListResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.createdAt,
                        productImage.imageUrl
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .leftJoin(favorite)
                .on(product.id.eq(favorite.product.id))
                .where(product.state.eq(productState))
                .groupBy(product.id, productImage.id)
                .orderBy(favorite.id.count().desc(), product.id.desc())
                .limit(20)
                .offset(0)
                .fetch();
    }

    @Override
    public List<ProductListResponseDto> getTopViewProductList() {

        return jpaQueryFactory
                .select(new QProductListResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.createdAt,
                        productImage.imageUrl
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id).and(productImage.thumbnail.eq(true)))
                .where(product.state.eq(ProductState.ON_SALE))
                .orderBy(product.viewCount.desc(), product.id.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<ProductListResponseDto> getTopKeywordProductList(String firstKeyword) {
        return jpaQueryFactory
                .select(new QProductListResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.createdAt,
                        productImage.imageUrl
                ))
                .from(product)
                .leftJoin(productImage)
                .on(product.id.eq(productImage.product.id)
                        .and(productImage.thumbnail.eq(true)))
                .where(product.state.eq(ProductState.ON_SALE)
                        .and(product.name.containsIgnoreCase(firstKeyword)))
                .orderBy(product.viewCount.desc(), product.id.desc())
                .limit(20)
                .fetch();
    }

    public Long countMyProducts(String userId, ProductState state, Long limit, String searchWord) {
        return jpaQueryFactory
                .select(product.id.count())
                .from(product)
                .where(product.user.id.eq(userId)
                        .and(state == ProductState.ALL ? null : product.state.eq(state))
                        .and(searchWord.isEmpty() ? null : product.name.containsIgnoreCase(searchWord))
                )
                .limit(limit)
                .fetchOne();
    }

    // 상품 정렬 (최신순, 낮은순, 고가순)
    public OrderSpecifier<?>[] productOrderSpecifier(String orderBy){
        if(orderBy.equals("recently")) {
            // 생성시간이 같으면 Id로 정렬
            return new OrderSpecifier[]{
                    product.upAt.desc(), product.id.desc()
            };
        }

        else if(orderBy.equals("low")) {
            return new OrderSpecifier[]{
                    product.price.asc(),
                    product.id.desc()  // 가격이 같으면 최신 상품 우선
            };
        }

        else if(orderBy.equals("high")) {
            return new OrderSpecifier[]{
                    product.price.desc(),
                    product.id.desc()
            };
        }

        else{
            return new OrderSpecifier[]{
                    product.id.desc()
            };
        }
    }

    public OrderSpecifier<?>[] favoriteProductOrderSpecifier(String orderBy){
        if(orderBy.equals("recently")) {
            // 생성시간이 같으면 Id로 정렬
            return new OrderSpecifier[]{
                    favorite.id.desc()
            };
        }

        else if(orderBy.equals("low")) {
            return new OrderSpecifier[]{
                    product.price.asc(),
                    favorite.id.desc()  // 가격이 같으면 최신 상품 우선
            };
        }

        else if(orderBy.equals("high")) {
            return new OrderSpecifier[]{
                    product.price.desc(),
                    favorite.id.desc()
            };
        }

        else{
            return new OrderSpecifier[]{
                    favorite.id.desc()
            };
        }
    }

    // 상품 검색 워드가 있으면 조건 추가
    public BooleanExpression productSearchWord(String searchWord){
        return searchWord != null && !searchWord.isEmpty() ? product.name.containsIgnoreCase(searchWord) : null;
    }

    // 상품 카테고리가 0 이면 전체, 아니면 해당 카테고리 상품 조건추가
    public BooleanExpression productCategory(Long categoryId){

        return categoryId == 0 ? null : product.category.id.eq(categoryId);
    }

    // 상품 ALL, NULL 일 경우 전체상품, 아니면 판매중, 판매완료 등의 조건추가
    public BooleanExpression productState(ProductState state){

        return state.equals(ProductState.ALL) || state == null ? null : product.state.eq(state);
    }

    public BooleanExpression favoritePaginationCursorFilter(String orderBy, Long lastIndex, Long lastPrice){
        // 마지막 인덱스, 마지막 가격 없는 경우 (맨 처음 데이터호출할 때)
        if(lastIndex == null || lastPrice == null) return null;

        else if (orderBy.equals("recently")) {
            // 찜한 순서대로 보여주기
            return favorite.id.lt(lastIndex);
        }

        else if(orderBy.equals("low")){
            // 마지막인덱스 밑 id + 마지막 가격보다 높거나 같은가격
            return product.price.gt(lastPrice).or(product.price.eq(lastPrice).and(favorite.id.lt(lastIndex)));
        }

        else if(orderBy.equals("high")){
            // 마지막인덱스 밑 id + 마지막 가격보다 낮거나 같은가격
            return product.price.lt(lastPrice).or(product.price.eq(lastPrice).and(favorite.id.lt(lastIndex)));
        }

        else return null;
    }

    public BooleanExpression productPaginationCursorFilter(String orderBy, Long lastIndex, Long lastPrice){
        // 마지막 인덱스, 마지막 가격 없는 경우 (맨 처음 데이터호출할 때)
        if(lastIndex == null || lastPrice == null) return null;

        else if (orderBy.equals("recently")) {
            // 마지막인덱스 밑에 있는 데이터들을 가져와야함
            return product.id.lt(lastIndex);
        }

        else if(orderBy.equals("low")){
            // 마지막인덱스 밑 id + 마지막 가격보다 높거나 같은가격
            return product.price.gt(lastPrice).or(product.price.eq(lastPrice).and(product.id.lt(lastIndex)));
        }

        else if(orderBy.equals("high")){
            // 마지막인덱스 밑 id + 마지막 가격보다 낮거나 같은가격
            return product.price.lt(lastPrice).or(product.price.eq(lastPrice).and(product.id.lt(lastIndex)));
        }

        else return null;
    }
}
