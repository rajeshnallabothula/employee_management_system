package com.brinta.ems.exception.exceptionHandler;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException (String message) {
        super(message);
    }

}
