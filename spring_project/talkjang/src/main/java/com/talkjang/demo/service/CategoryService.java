package com.talkjang.demo.service;

import com.talkjang.demo.common.enums.ErrorCode;
import com.talkjang.demo.dto.response.CommonResponseDto;
import com.talkjang.demo.dto.response.category.CategoryAllResponseDto;
import com.talkjang.demo.dto.response.category.CategoryResponseDto;
import com.talkjang.demo.entity.Category;
import com.talkjang.demo.handler.CustomException;
import com.talkjang.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CommonResponseDto read(Long categoryId){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        return CommonResponseDto.success(CategoryResponseDto.from(category));
    }

    public CommonResponseDto readAll(){

        return CommonResponseDto.success(categoryRepository.findAll().stream()
                .map(category -> CategoryAllResponseDto.from(category))
                .toList());
    }

}
