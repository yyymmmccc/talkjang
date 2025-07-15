package com.talkjang.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@Builder
public class CommonResponseDto <T>{

    private String code;
    private String message;
    private T data;

    public static <T> CommonResponseDto success(T data){
        return CommonResponseDto.builder()
                .code("SUCCESS")
                .message("성공")
                .data(data)
                .build();
    }

    public static CommonResponseDto error(String code, String message){
        return CommonResponseDto.builder()
                .code(code)
                .message(message)
                .build();
    }

}
