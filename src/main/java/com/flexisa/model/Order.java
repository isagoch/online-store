package com.flexisa.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(String id, Customer customer) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Order ID must not be blank");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }

        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item must not be null");
        }
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Items can only be added to a PENDING order");
        }
        this.items.add(item);
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.subtotal();
    }
        return total;
    }


}
