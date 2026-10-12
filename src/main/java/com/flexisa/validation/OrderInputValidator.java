package com.flexisa.validation;

public final class OrderInputValidator {

    private OrderInputValidator() {
    }

    public static void validateOrder(double price, int quantity, String location) {
        if (price <= 0) {
            throw new IllegalArgumentException("Please input a price greater than zero");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Please input quantity greater than zero");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Please input a valid location");
        }
    }
    
}