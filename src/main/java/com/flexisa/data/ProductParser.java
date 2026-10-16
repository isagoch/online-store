package com.flexisa.data;

import com.flexisa.model.Product;

public final class ProductParser {

    private ProductParser() {
    }

    public static Product parseLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Product line must not be blank");
        }

        String[] parts = line.split(",", -1);

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid product line: " + line);
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String id = parts[0];
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);
        int stock = Integer.parseInt(parts[3]);

        return new Product(id, name, price, stock);
    }
}