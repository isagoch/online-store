package com.flexisa.model;

public class Product {
    private final String id;
    private final String name;
    private int stockQuantity;
    private final double price;

    public Product(String id, String name, double price, int stockQuantity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }
        if (Double.isNaN(price) || price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity must not be negative");
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
    return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be positive");
        }
        if (quantity > stockQuantity) {
            throw new IllegalStateException("Insufficient stock to reduce by " + quantity);
        }
        this.stockQuantity -= quantity;
    }
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be positive");
        }
        this.stockQuantity += quantity;
    }

}

