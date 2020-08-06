package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.api.LoggingController;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
@ControllerAdvice
public class RsEventExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RsEventExceptionHandler.class);
    @ExceptionHandler({RequestNotValidException.class,RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsEventNotValidExceptionHandler(Exception e){
        String errorMessage;
        if (e instanceof RsEventNotValidException || e instanceof RequestNotValidException){
            errorMessage = e.getMessage();
        } else {
            errorMessage = "invalid user";
        }
        Error error = new Error();
        error.setError(errorMessage);
        logger.error(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
