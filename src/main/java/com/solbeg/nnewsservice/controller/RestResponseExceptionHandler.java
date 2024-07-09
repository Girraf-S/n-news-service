package com.solbeg.nnewsservice.controller;


import com.solbeg.nnewsservice.exception.HeaderException;
import com.solbeg.nnewsservice.model.Response;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(HeaderException.class)
    @AfterThrowing(pointcut = "execution(* com.solbeg.nuserservice.controller.*.*(..))", throwing = "HeaderException")
    public ResponseEntity<Response> handleConflict(
            HeaderException ex) {
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    @AfterThrowing(pointcut = "execution(* com.solbeg.nuserservice.controller.*.*(..))", throwing = "HeaderException")
    public ResponseEntity<Response> handleConflict(
            RuntimeException ex) {
        Response response = new Response(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
