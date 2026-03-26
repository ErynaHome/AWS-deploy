package com.yonsai.geminiAi.exception;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 모든 Controller에서 발생하는 예외를 한 곳에서 처리
 * 예외가 발생하면 한곳으로 모인다.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleVaildationException(){
        
    }
}
