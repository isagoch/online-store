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

    public void startProcessing() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order can only start processing when its status is PENDING");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item before processing");
        }
        status = OrderStatus.PROCESSING;
    }

    public void ship() {
        if (status != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Order can only be shipped when its status is PROCESSING");
        }
        status = OrderStatus.SHIPPED;
    }
    public void deliver() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order can only be delivered when its status is SHIPPED");
        }
        status = OrderStatus.DELIVERED;
    }
    public void cancel() {
        boolean canCancel = status == OrderStatus.PENDING || status == OrderStatus.PROCESSING;
        if (!canCancel) {
            throw new IllegalStateException("Only PENDING or PROCESSING orders can be cancelled");
        }
        status = OrderStatus.CANCELLED;
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
