package com.talkjang.demo.controller;

import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public CommonResponseDto read(@PathVariable("categoryId") Long categoryId){

        return categoryService.read(categoryId);
    }

    @GetMapping("/all")
    public CommonResponseDto readAll(){

        return categoryService.readAll();
    }
}
