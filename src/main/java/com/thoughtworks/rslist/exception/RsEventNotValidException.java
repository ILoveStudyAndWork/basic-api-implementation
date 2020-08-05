package com.thoughtworks.rslist.exception;

public class RsEventNotValidException extends RuntimeException{
    private String Message;
    public RsEventNotValidException(String Message) {
        this.Message = Message;
    }

    @Override
    public String getMessage() {
        return this.Message;
    }
}
