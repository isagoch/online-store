package com.flexisa.service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import com.flexisa.model.Order;
import com.flexisa.model.OrderStatus;
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

    public List<Order> filterOrdersByStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status must not be null");
        }
        return orders.stream()
                .filter(order -> order.getStatus() == status)
                .toList();
    }

    public List<Order> filterOrdersByCustomerId(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID must not be null or blank");
        }
        String normalizedCustomerId = customerId.trim();
        return orders.stream()
                .filter(order -> order.getCustomer().getId().equals(normalizedCustomerId))
                .toList();
    }

    public List<Order> filterOrdersByTotalRange(double min, double max) {
        if (Double.isNaN(min) || Double.isNaN(max)) {
            throw new IllegalArgumentException("Total range bounds must not be NaN");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum total must not exceed maximum total");
        }
        return orders.stream()
                // This is the plain item total; cancelled orders are not excluded.
                .filter(order -> {
                    double total = order.calculateTotal();
                    return total >= min && total <= max;
                })
                .toList();
    }

    public List<Product> sortProductsByPrice() {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .toList();
    }

    public List<Product> sortProductsByName() {
        return products.stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public List<Product> sortProductsByStockQuantity() {
        return products.stream()
                .sorted(Comparator.comparingInt(Product::getStockQuantity))
                .toList();
    }

    public List<Product> removeDuplicateProducts() {
        return List.copyOf(new LinkedHashSet<>(products));
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
