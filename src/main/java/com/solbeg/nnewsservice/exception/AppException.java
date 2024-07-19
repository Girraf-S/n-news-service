package com.solbeg.nnewsservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AppException extends RuntimeException{
    private String message;
    private final HttpStatus httpStatus;
}
