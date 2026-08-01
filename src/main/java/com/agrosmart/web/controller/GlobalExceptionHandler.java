package com.agrosmart.web.controller;

import com.agrosmart.domain.exception.ProductoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return Map.of("error", ex.getMessage());
    }
}
