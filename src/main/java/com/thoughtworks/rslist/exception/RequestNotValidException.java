package com.thoughtworks.rslist.exception;

public class RequestNotValidException extends RuntimeException{
    private String message;

    public RequestNotValidException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
