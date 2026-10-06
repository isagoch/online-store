package com.flexisa.model;

public class Customer {
    private final String id;
    private final String name;
    private final String email;
    private final Address address;

    public Customer(String id, String name, String email, Address address) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Customer ID must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be blank");
        }
        if (email == null || email.isBlank() || email.contains("@") == false) {
            throw new IllegalArgumentException("Email is incorrect");
        }
        if (address == null) {
            throw new IllegalArgumentException("Customer address must not be null");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }
}
