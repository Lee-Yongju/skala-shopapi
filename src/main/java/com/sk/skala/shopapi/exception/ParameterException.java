package com.sk.skala.shopapi.exception;

public class ParameterException extends ResponseException {
    public ParameterException(String message) {
        super(Error.INVALID_PARAMETER, message);
    }
}
