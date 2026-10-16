package com.flexisa.data;

import com.flexisa.model.Address;
import com.flexisa.model.Customer;

public final class CustomerParser {

    private CustomerParser() {
    }

    public static Customer parseLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Customer line must not be blank");
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 8) {
            throw new IllegalArgumentException("Invalid customer line: " + line);
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        Address address = new Address(parts[3], parts[4], parts[5], parts[6], parts[7]);
        return new Customer(parts[0], parts[1], parts[2], address);
    }
}
