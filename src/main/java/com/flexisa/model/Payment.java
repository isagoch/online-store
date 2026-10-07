package com.flexisa.model;

public class Payment {
    private final String id;
    private final Order order;
    private final double amount;
    private PaymentStatus status;


    public Payment(String id, Order order, double amount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Payment ID must not be blank");
        }
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (amount <= 0 || Double.isNaN(amount)) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        this.id = id;
        this.order = order;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public void complete() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment can only be completed when its status is PENDING");
        }
        status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Payment can only be failed when its status is PENDING");
        }
        status = PaymentStatus.FAILED;
    }

    public void refund() {
        if (status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment can only be refunded when its status is COMPLETED");
        }
        status = PaymentStatus.REFUNDED;
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
