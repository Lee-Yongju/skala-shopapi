package com.sk.skala.shopapi.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum Error {
    SUCCESS(HttpStatus.OK, "정상 처리되었습니다."),

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 파라미터가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다."),
    DATA_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    INSUFFICIENT_FUNDS(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다."),
    INSUFFICIENT_QUANTITY(HttpStatus.BAD_REQUEST, "취소할 수량이 주문 수량보다 많습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    Error(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
