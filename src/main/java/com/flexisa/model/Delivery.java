package com.flexisa.model;

public class Delivery {
    private final String id;
    private final Order order;
    private final Address address;
    private DeliveryStatus status;

    public Delivery(String id, Order order, Address address) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Delivery ID must not be blank");
        }
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address must not be null");
        }

        this.id = id;
        this.order = order;
        this.address = address;
        this.status = DeliveryStatus.PENDING;
    }

    public void dispatch() {
        if (status != DeliveryStatus.PENDING) {
            throw new IllegalStateException("Delivery can only be dispatched when its status is PENDING");
        }
        status = DeliveryStatus.IN_TRANSIT;
    }

    public void markDelivered() {
        if (status != DeliveryStatus.IN_TRANSIT) {
            throw new IllegalStateException("Delivery can only be marked as delivered when its status is IN_TRANSIT");
        }
        status = DeliveryStatus.DELIVERED;
    }

    public void markReturned() {
        if (status != DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("Delivery can only be marked as returned when its status is DELIVERED");
        }
        status = DeliveryStatus.RETURNED;
    }

    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Address getAddress() {
        return address;
    }

    public DeliveryStatus getStatus() {
        return status;
    }
}
