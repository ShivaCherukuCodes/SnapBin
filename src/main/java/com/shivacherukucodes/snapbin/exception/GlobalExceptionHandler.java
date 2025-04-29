package com.shivacherukucodes.snapbin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle specific file-related exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleFileException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("File Error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("General Error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> handleFileStorageException(FileStorageException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse("File Storage Error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
