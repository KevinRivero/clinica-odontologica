package com.Integrador.exception;

import com.Integrador.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException{
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> exceptionNotFound(ResourceNotFoundException mensaje){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> exceptionBadRequest(BadRequestException mensaje){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje.getMessage());
    }
}
