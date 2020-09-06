package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.DateNotValidException;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ControllerAdvice
public class RsEventExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RsEventExceptionHandler.class);

    @ExceptionHandler({RequestNotValidException.class, RsEventNotValidException.class,
                        MethodArgumentNotValidException.class, DateNotValidException.class})
    public ResponseEntity rsEventNotValidExceptionHandler(Exception e){
        String errorMessage = "";
        if (e instanceof RsEventNotValidException || e instanceof RequestNotValidException || e instanceof DateNotValidException){
            errorMessage = e.getMessage();
        } else {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            String errorFiled = methodArgumentNotValidException.getBindingResult().getFieldError().getField();//user.userName
            if(specifyErrorFrom(errorFiled).equals("RsEvent")){
                errorMessage = "invalid param";
            }else if(specifyErrorFrom(errorFiled).equals("user")){
                errorMessage = "invalid user";
            }
        }
        Error error = new Error();
        error.setError(errorMessage);
        logger.error(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

    private String specifyErrorFrom(String errorFiled) {
        String pattern = "^user\\..*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(errorFiled);
        if (m.matches()){
            return "user";
        }else {
            return "RsEvent";
        }
    }
}
