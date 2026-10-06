package com.flexisa.model;

public record Address(String street, String city, String state, String country, String postalCode) {

    public Address {
        requireText(street, "street");
        requireText(city, "city");
        requireText(state, "state");
        requireText(country, "country");
        requireText(postalCode, "postalCode");
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}