package com.test.blazebackend.exception;

import java.util.Map;

public class ServiceException extends RuntimeException {
    private int code;
    private Map response;

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(int code, String message, Map response) {
        super(message);
        this.code = code;
        this.response = response;
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map getResponse() {
        return this.response;
    }

    public void setResponse(Map response) {
        this.response = response;
    }
}
