package com.flexisa.model;

public record OrderItem(Product product, int quantity) {
    public OrderItem {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Product must not be null and quantity must be positive");
        }
    }

    public double subtotal() {
        return product.getPrice() * quantity;
    }
}
