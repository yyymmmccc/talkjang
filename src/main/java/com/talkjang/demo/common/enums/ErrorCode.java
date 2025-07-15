package com.talkjang.demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    BAD_REQUEST_ORDER_USER(HttpStatus.BAD_REQUEST, "BAD_REQUEST_ORDER_USER", "주문자와 유저가 일치하지 않습니다."),
    BAD_REQUEST_PAYMENT(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "결제금액을 확인해 주세요."),
    BAD_REQUEST_UPDATE_PASSWORD(HttpStatus.BAD_REQUEST, "BAD_REQUEST_UPDATE_PASSWORD", "비밀번호를 확인해주세요."),
    BAD_REQUEST_ORDER_STATE(HttpStatus.BAD_REQUEST, "BAD_REQUEST_ORDER_STATE", "주문정보를 조회 할 수 없습니다."),
    BAD_REQUEST_ORDER_CANCEL(HttpStatus.BAD_REQUEST, "BAD_REQUEST_ORDER_CANCEL", "이미 취소됐거나 취소할 수 없는 주문입니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "권한이 없습니다."),

    NOT_IMAGE_FILE(HttpStatus.NOT_FOUND, "NOT_IMAGE_FILE", "이미지 파일을 올려주세요."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOT_FOUND_USER", "사용자를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "NOT_FOUND_PRODUCT", "상품을 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "NOT_FOUND_CATEGORY", "카테고리를 찾을 수 없습니다."),
    NOT_FOUND_FAVORITE_PRODUCT(HttpStatus.NOT_FOUND, "NOT_FOUND_CATEGORY", "찜한 상품을 찾을 수 없습니다."),
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "NOT_FOUND_CHAT_ROOM", "대화방을 찾을 수 없습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "NOT_FOUND_ORDER", "주문번호가 존재하지 않습니다."),
    NOT_FOUND_KAKAOPAY_TID(HttpStatus.NOT_FOUND, "NOT_FOUND_KAKAOPAY_TID", "카카오페이 tid가 존재하지않습니다."),

    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "DUPLICATE_USER_ID", "이미 사용 중인 아이디입니다."),
    DUPLICATE_SHOP_NAME(HttpStatus.CONFLICT, "DUPLICATE_SHOP_NAME", "이미 사용 중인 상점 이름입니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_USER_EMAIL", "이미 사용 중인 이메일입니다."),
    DUPLICATE_USER_PHONE(HttpStatus.CONFLICT, "DUPLICATE_USER_PHONE", "이미 사용 중인 전화번호입니다."),

    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "PRODUCT_UP_LIMIT_EXCEEDED", "업 가능 횟수를 초과했습니다.");

    HttpStatus status;
    String code;
    String message;
}
