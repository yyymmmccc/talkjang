package com.talkjang.demo.handler;

import com.talkjang.demo.dto.response.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customExceptionHandle(CustomException e){

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(CommonResponseDto.error(
                        e.getErrorCode().getCode(), e.getErrorCode().getMessage())
                );
    }
}
