package com.talkjang.demo.dto.response.category;

import com.talkjang.demo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAllResponseDto {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static CategoryAllResponseDto from(Category category){
        return CategoryAllResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
