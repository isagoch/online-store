package com.flexisa.service;

import java.util.List;
import java.util.Locale;

import com.flexisa.model.Order;
import com.flexisa.model.Product;

public class SalesAnalyser {

    private final List<Product> products;
    private final List<Order> orders;

    public SalesAnalyser(List<Product> products, List<Order> orders) {
        this.products = List.copyOf(products);
        this.orders = List.copyOf(orders);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be null or blank");
        }

        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        return products.stream()
                .filter(product -> product.getName().toLowerCase(Locale.ROOT)
                        .contains(normalizedKeyword))
                .toList();
    }

    public List<Order> getOrders() {
        return orders;
    }
}
