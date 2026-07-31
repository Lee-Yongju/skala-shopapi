package com.sk.skala.shopapi.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum Error {
    SUCCESS(HttpStatus.OK, "정상 처리되었습니다."),

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다."),
    DUPLICATE_CUSTOMER_ID(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    Error(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
