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
        String normalizedKeyword = normalizeKeyword(keyword);
        return products.stream()
                .filter(product -> containsIgnoreCase(product.getName(), normalizedKeyword))
                .toList();
    }

    public List<Order> searchOrders(String keyword) {
        String normalizedKeyword = normalizeKeyword(keyword);
        return orders.stream()
                .filter(order -> containsIgnoreCase(order.getId(), normalizedKeyword)
                        || containsIgnoreCase(order.getCustomer().getName(), normalizedKeyword))
                .toList();
    }

    private static String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be null or blank");
        }
        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean containsIgnoreCase(String text, String normalizedKeyword) {
        return text.toLowerCase(Locale.ROOT).contains(normalizedKeyword);
    }
}
