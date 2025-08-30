package com.brinta.ems.exception;

import com.brinta.ems.exception.exceptionHandler.*;
import com.brinta.ems.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<GenericResponse> handleDuplicateEntry(DuplicateEntryException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<GenericResponse> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.badRequest().body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GenericResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(UnAuthException.class)
    public ResponseEntity<GenericResponse> handleUnAuth(UnAuthException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(LevelNotFoundException.class)
    public ResponseEntity<GenericResponse> handleLevelNotFound(LevelNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(InvalidLevelException.class)
    public ResponseEntity<GenericResponse> handleInvalidLevel(InvalidLevelException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateMappingException.class)
    public ResponseEntity<GenericResponse> handleDuplicateMapping(DuplicateMappingException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericResponse(false, "Something went wrong: " + ex.getMessage()));
    }
}