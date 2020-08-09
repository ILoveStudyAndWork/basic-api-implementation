package com.thoughtworks.rslist.exception;

public class DateNotValidException extends Exception {
    private String message;
    public DateNotValidException(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
