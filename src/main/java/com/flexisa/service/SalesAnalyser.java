package com.flexisa.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.flexisa.model.Order;
import com.flexisa.model.OrderItem;
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

    public double calculateTotalSales() {
        return salesOrders().stream()
                .mapToDouble(Order::calculateTotal)
                .sum();
    }

    public double calculateAverageOrderValue() {
        return salesOrders().stream()
                .mapToDouble(Order::calculateTotal)
                .average()
                .orElse(0.0);
    }

    public List<ProductSales> findBestSellingProducts(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than zero");
        }

        Comparator<ProductSales> salesOrder = Comparator
                .comparingInt(ProductSales::unitsSold)
                .reversed()
                .thenComparing(sales -> sales.product().getId());
        return salesOrders().stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::product,
                        Collectors.summingInt(OrderItem::quantity)))
                .entrySet().stream()
                .map(entry -> new ProductSales(entry.getKey(), entry.getValue()))
                .sorted(salesOrder)
                .limit(limit)
                .toList();
    }

    public String buildSalesSummary(int topProductsLimit) {
        List<Order> uniqueOrders = uniqueOrders();
        List<Order> qualifyingOrders = salesOrders();
        int duplicateRecords = orders.size() - uniqueOrders.size();
        int cancelledOrders = uniqueOrders.size() - qualifyingOrders.size();
        List<ProductSales> bestSellingProducts = findBestSellingProducts(topProductsLimit);

        List<String> lines = new ArrayList<>();
        lines.add(String.format(
                Locale.ROOT,
                "Orders analysed: %d (%d records excluded: %d duplicate, %d cancelled)",
                qualifyingOrders.size(),
                duplicateRecords + cancelledOrders,
                duplicateRecords,
                cancelledOrders));
        lines.add(String.format(Locale.ROOT, "Total sales: #%.2f", calculateTotalSales()));
        lines.add(String.format(
                Locale.ROOT, "Average order value: #%.2f", calculateAverageOrderValue()));
        lines.add(String.format(Locale.ROOT, "Top %d products by units sold:", topProductsLimit));
        if (bestSellingProducts.isEmpty()) {
            lines.add("No products sold");
        } else {
            bestSellingProducts.forEach(sales -> lines.add(String.format(
                    Locale.ROOT,
                    "%s %s (%d %s)",
                    sales.product().getId(),
                    sales.product().getName(),
                    sales.unitsSold(),
                    sales.unitsSold() == 1 ? "unit" : "units")));
        }
        return String.join("\n", lines);
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

    public List<String> findDuplicateOrderIds() {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new LinkedHashSet<>();
        for (Order order : orders) {
            if (!seen.add(order.getId())) {
                duplicates.add(order.getId());
            }
        }
        return List.copyOf(duplicates);
    }

    public List<Order> removeDuplicateOrders() {
        return List.copyOf(new LinkedHashSet<>(orders));
    }

    public Map<OrderStatus, List<Order>> groupOrdersByStatus() {
        return uniqueOrders().stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    public Map<String, List<Order>> groupOrdersByCustomerId() {
        return uniqueOrders().stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    private List<Order> uniqueOrders() {
        return removeDuplicateOrders();
    }

    private List<Order> salesOrders() {
        return uniqueOrders().stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
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
