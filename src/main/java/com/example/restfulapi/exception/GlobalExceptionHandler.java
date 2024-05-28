package com.example.restfulapi.exception;

import com.example.restfulapi.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<WebResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type is '%s'.",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());

        WebResponse<String> response = WebResponse.<String>builder()
                .errors(errorMessage)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
