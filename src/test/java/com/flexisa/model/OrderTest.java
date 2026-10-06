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
}
