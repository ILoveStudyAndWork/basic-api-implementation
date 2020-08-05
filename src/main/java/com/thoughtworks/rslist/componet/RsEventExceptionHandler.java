package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {
    @ExceptionHandler({RequestNotValidException.class,RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsEventNotValidExceptionHandler(Exception e){
        String errorMessage;
        if (e instanceof RsEventNotValidException || e instanceof RequestNotValidException){
            errorMessage = e.getMessage();
        } else {
            errorMessage = "invalid rsEvent";
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
