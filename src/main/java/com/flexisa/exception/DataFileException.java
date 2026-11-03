package com.flexisa.exception;

public class DataFileException extends Exception {
    public DataFileException(String message) {
        super(message);
    }

    public DataFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
