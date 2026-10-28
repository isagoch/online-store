package com.flexisa.exception;

public class InvalidPaymentStateException extends IllegalStateException {
    public InvalidPaymentStateException(String message) {
        super(message);
    }
}
