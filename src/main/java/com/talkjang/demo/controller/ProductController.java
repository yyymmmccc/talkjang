package com.talkjang.demo.controller;

import com.talkjang.demo.dto.request.product.ProductRequestDto;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public CommonResponseDto create(@AuthenticationPrincipal String userId,
                                    @RequestBody ProductRequestDto dto){

        return productService.create(userId, dto);
    }

    @PatchMapping("/{productId}/update")
    public CommonResponseDto update(@PathVariable("productId") Long productId,
                                    @RequestBody ProductRequestDto dto){

        return productService.update(productId, dto);
    }

    @DeleteMapping("/{productId}/delete")
    public CommonResponseDto delete(@AuthenticationPrincipal String userId,
                                    @PathVariable("productId") Long productId){

        return productService.delete(userId, productId);
    }

    @GetMapping("/{productId}")
    public CommonResponseDto getProductDetail(@AuthenticationPrincipal String userId,
                                              @PathVariable("productId") Long productId){

        return productService.getProductDetail(userId, productId);
    }

    // /list/1?page=1
    @GetMapping("/list")
    public CommonResponseDto getProductList(@RequestParam(name = "categoryId", defaultValue = "0") Long categoryId,
                                     @RequestParam(name = "page", defaultValue = "1") Long page,
                                     @RequestParam(name = "pageSize", defaultValue = "20") Long pageSize,
                                     @RequestParam(name = "orderBy", defaultValue = "recently") String orderBy,
                                     @RequestParam(name = "searchWord", defaultValue = "") String searchWord){

        return productService.getProductList(categoryId, page, pageSize, orderBy, searchWord);
    }

    // localhost:8080/api/products/list/favorite
    @GetMapping("/list/favorite")
    public CommonResponseDto getTopFavoriteProductList(){

        return productService.getTopFavoriteProductList();
    }

    @GetMapping("/list/view")
    public CommonResponseDto getTopViewProductList(){

        return productService.getTopViewProductList();
    }

    @GetMapping("/list/hot-keyword")
    public CommonResponseDto getHotKeywordProductList(){

        return productService.getHotKeywordProductList();
    }

    @GetMapping("/ranking")
    public CommonResponseDto getTopSearchWordRankings(){

        return productService.getTopSearchWordRankings();
    }
}
