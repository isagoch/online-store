package com.flexisa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class OrderTest {
    Product product = new Product("1", "Test Product", 10.0, 100);
    private final Address lagos = new Address("12 Allen Ave", "Ikeja", "Lagos", "Nigeria", "100001");
    Customer customer = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);

    @Test
    void newOrderStartsPendingWithNoItems() {
        Order order = new Order("order-1", customer);

        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void invalidOrder_withBlankId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
        new Order(" ", customer)
    );
        assertEquals("Order ID must not be blank", exception.getMessage());
    }

    @Test
    void invalidOrder_withNullCustomer() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
        new Order("order-1", null)
    );
        assertEquals("Customer must not be null", exception.getMessage());
    }

    @Test
    void addingItemCalculatesCorrectTotal() {
        Order order = new Order("order-1", customer);
        order.addItem(new OrderItem(product, 3));

        assertEquals(30.0, order.calculateTotal());
    }

    @Test
    void getItemsReturnsACopy() {
        Order order = new Order("order-1", customer);
        var returnedItems = order.getItems();

        returnedItems.add(new OrderItem(product, 1));

        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void orderMovesFromPendingToProcessingToShippedToDelivered() {
        Order order = new Order("order-1", customer);
        order.addItem(new OrderItem(product, 1));

        assertEquals(OrderStatus.PENDING, order.getStatus());

        order.startProcessing();
        assertEquals(OrderStatus.PROCESSING, order.getStatus());

        order.ship();
        assertEquals(OrderStatus.SHIPPED, order.getStatus());

        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    void cancelWorksFromPending() {
        Order order = new Order("order-1", customer);

        order.cancel();

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelIsRejectedWhenAlreadyCancelled() {
        Order order = new Order("order-1", customer);
        order.cancel();

        IllegalStateException exception = assertThrows(IllegalStateException.class, order::cancel);

        assertEquals("Only PENDING or PROCESSING orders can be cancelled", exception.getMessage());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelIsRejectedOnceShipped() {
        Order order = new Order("order-1", customer);
        order.addItem(new OrderItem(product, 1));
        order.startProcessing();
        order.ship();

        IllegalStateException exception = assertThrows(IllegalStateException.class, order::cancel);

        assertEquals("Only PENDING or PROCESSING orders can be cancelled", exception.getMessage());
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shipIsRejectedFromPending() {
        Order order = new Order("order-1", customer);

        IllegalStateException exception = assertThrows(IllegalStateException.class, order::ship);

        assertEquals("Order can only be shipped when its status is PROCESSING", exception.getMessage());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void addItemIsRejectedAfterProcessingStarts() {
        Order order = new Order("order-1", customer);
        order.addItem(new OrderItem(product, 1));
        order.startProcessing();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.addItem(new OrderItem(product, 1)));

        assertEquals("Items can only be added to a PENDING order", exception.getMessage());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void startProcessingIsRejectedForEmptyOrder() {
        Order order = new Order("order-1", customer);

        IllegalStateException exception = assertThrows(IllegalStateException.class, order::startProcessing);

        assertEquals("Order must contain at least one item before processing", exception.getMessage());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }
}
