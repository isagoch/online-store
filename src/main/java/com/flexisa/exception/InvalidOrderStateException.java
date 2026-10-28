package com.flexisa.exception;

public class InvalidOrderStateException extends IllegalStateException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
