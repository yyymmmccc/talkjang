package com.talkjang.demo.service;

import com.talkjang.demo.common.PagingCalculator;
import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.common.enums.ProductState;
import com.talkjang.demo.dto.request.product.ProductRequestDto;
import com.talkjang.demo.dto.request.product.ProductImageRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.product.ProductDetailResponseDto;
import com.talkjang.demo.dto.response.product.ProductHotKeywordListResponseDto;
import com.talkjang.demo.dto.response.product.ProductListResponseDto;
import com.talkjang.demo.dto.response.product.ProductPageResponseDto;
import com.talkjang.demo.entity.Category;
import com.talkjang.demo.entity.Product;
import com.talkjang.demo.entity.ProductImage;
import com.talkjang.demo.entity.User;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.CategoryRepository;
import com.talkjang.demo.repository.ProductImageRepository;
import com.talkjang.demo.repository.ProductRepository;
import com.talkjang.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductViewRedisService productViewRedisService;
    private final ProductSearchRankingRedisService productSearchRankingRedisService;

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CommonResponseDto create(String userId, ProductRequestDto dto){

        User user =  findByUserOrThrow(userId);

        Category category = findByCategoryOrThrow(dto.getCategoryId());

        Product product = productRepository.save(dto.toEntity(user, category));

        List<ProductImage> productImages = convertToProductImages(dto.getProductImageRequestDtoList(), product);
        // 이미지를 저장할 떄 PK 가 AUTO_INCREMENT 이기 때문에 bulk insert 가 아닌 개별적으로 쿼리를 날림
        productImageRepository.saveAll(productImages);

        return CommonResponseDto.success(product.getId());
    }

    @Transactional
    public CommonResponseDto update(Long productId, ProductRequestDto dto){

        Product product = findByProductOrThrow(productId);

        // 유저 아이디와 상품의 등록자 equals 해줘서 다르면 throw 날려주기

        Category category = findByCategoryOrThrow(dto.getCategoryId());

        product.update(dto, category);

        productImageRepository.deleteByProduct(product);

        List<ProductImage> productImages = convertToProductImages(dto.getProductImageRequestDtoList(), product);
        // 이미지를 저장할 떄 PK 가 AUTO_INCREMENT 이기 때문에 bulk insert 가 아닌 개별적으로 쿼리를 날림
        productImageRepository.saveAll(productImages);

        return CommonResponseDto.success(null);
    }

    @Transactional
    public CommonResponseDto delete(String userId, Long productId) {

        Product product = findByProductOrThrow(productId);
        if(!product.getUser().getId().equals(userId))
            throw new CustomException(ErrorCode.FORBIDDEN);

        productImageRepository.deleteByProduct(product);
        productRepository.delete(product);

        return CommonResponseDto.success(null);
    }

    @Transactional
    public CommonResponseDto getProductDetail(String userId, Long productId){
        ProductDetailResponseDto product = productRepository.findProductDetailsWithFavoriteCountAndFavoriteStatus(userId, productId);

        if(!userId.equals("anonymousUser") && productViewRedisService.validateProductView(userId, productId)){
            // 로그인 되어 있고 30분내에 본적 없는경우 1증가, 아니면 pass
            Long viewCount = productViewRedisService.increaseProductView(productId);
            // 증가 시킨게 100 으로 나누어 떨어지면 백업

            if(viewCount % 10 == 0){
                productRepository.productViewCountBackUp(productId, viewCount);
            }
        }

        Long viewCount = productViewRedisService.readProductViewCount(productId);
        product.setViewCount(viewCount);

        return CommonResponseDto.success(product);
    }

    public CommonResponseDto getProductList(Long categoryId, Long page, Long pageSize, String orderBy, String searchWord){

        List<ProductListResponseDto> productList =
                productRepository.findAllByCategoryAndImageThumbnail(categoryId, (page - 1) * pageSize, pageSize, orderBy, searchWord);
        Long pageCount = productRepository.pageCount(PagingCalculator.pageCountCalc(page, pageSize), categoryId, searchWord);

        if(!searchWord.isEmpty()){
            productSearchRankingRedisService.increaseSearchWordScore(searchWord);
        }

        return CommonResponseDto.success(ProductPageResponseDto.of(productList, pageCount));
    }

    public CommonResponseDto getTopSearchWordRankings() {

        return CommonResponseDto.success(productSearchRankingRedisService.getTopSearchWordRankingList());
    }

    public CommonResponseDto getTopFavoriteProductList() {

        List<ProductListResponseDto> list = productRepository.getTopFavoriteProductList(ProductState.ON_SALE);

        return CommonResponseDto.success(list);
    }

    public CommonResponseDto getTopViewProductList() {

        List<ProductListResponseDto> list = productRepository.getTopViewProductList();

        return CommonResponseDto.success(list);
    }

    public CommonResponseDto getHotKeywordProductList() {

        Set<String> keywordSet = productSearchRankingRedisService.getTopSearchWordRankingList();

        String firstKeyword = keywordSet.iterator().next();

        List<ProductListResponseDto> list = productRepository.getTopKeywordProductList(firstKeyword);

        return CommonResponseDto.success(ProductHotKeywordListResponseDto.of(list, firstKeyword));
    }

    public Long countProductByUser(User user) {
        return productRepository.countProductByUserId(user.getId());
    }

    public Product findByProductOrThrow(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    public User findByUserOrThrow(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public Category findByCategoryOrThrow(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
    }

    public List<ProductImage> convertToProductImages(List<ProductImageRequestDto> productImageDtoList, Product product){
        List<ProductImage> productImages = new ArrayList<>();

        for(ProductImageRequestDto productImageDto : productImageDtoList){

            productImages.add(ProductImage.builder()
                    .imageUrl(productImageDto.getProductImageUrl())
                    .thumbnail(productImageDto.isThumbnail())
                    .product(product)
                    .build());
        }

        return productImages;
    }
}
