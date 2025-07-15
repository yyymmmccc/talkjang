package com.talkjang.demo.handler;


import com.talkjang.demo.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    private ErrorCode errorCode;

}
