package com.users.api.exception;

public class EmptySearchResultException extends RuntimeException {
    private final Integer count;
    private final String errorCode;
    private static final long serialVersionUID = -3489852400690354701L;

    public EmptySearchResultException(Integer count, String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.count = count;
    }

    public Integer getCount() {
        return this.count;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
