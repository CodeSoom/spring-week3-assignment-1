package com.codesoom.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 애플리케이션 전역에 공통으로 사용되는 Global Exception Handler 입니다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 메소드 Argument 타입이 일치하지 않는 경우 예외를 던집니다.
     * 
     * @param e MethodArgumentTypeMismatchException 예외
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Method Argument Type Mismatch Exception")
    public void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    }
}
