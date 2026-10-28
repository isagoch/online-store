package com.flexisa.exception;

public class InsufficientStockException extends IllegalStateException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
