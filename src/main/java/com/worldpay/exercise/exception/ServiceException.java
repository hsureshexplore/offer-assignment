package com.worldpay.exercise.exception;

public class ServiceException extends RuntimeException {
    private final String errorCode;

    public ServiceException(String message) {
        this(message, "INTERNAL_SERVER_ERROR");
    }

    public ServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}