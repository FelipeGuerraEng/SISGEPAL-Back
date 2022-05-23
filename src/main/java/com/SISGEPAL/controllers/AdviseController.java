package com.SISGEPAL.controllers;

import com.SISGEPAL.DTO.session.ErrorResponse;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviseController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundExceptio(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.UNAUTHORIZED);
    }
}
