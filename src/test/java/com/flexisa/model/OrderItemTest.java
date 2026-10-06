package com.flexisa.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OrderItemTest {
    @Test
    public void validOrderItemCreationTest() {
        Product product = new Product("1", "Test Product", 10.0, 100);
        OrderItem orderItem = new OrderItem(product, 5);
        assertNotNull(orderItem);
        orderItem.product();
        orderItem.quantity();
    }

    @Test
    public void invalidOrderItemCreation_withNullProductTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new OrderItem(null, 5)
        );
        assertEquals("Product must not be null and quantity must be positive", exception.getMessage());
    }

    @Test
    public void checkSubtotal_isCorrectTest() {
        Product product = new Product("1", "Test Product", 10.0, 100);
        OrderItem orderItem = new OrderItem(product, 5);
        assertEquals(50.0, orderItem.subtotal(), 0.01);
    }

    @Test
    public void orderItem_withZeroQuantity() {
        Product product = new Product("1", "Test Product", 10.0, 100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new OrderItem(product, 0)
        );
        assertEquals("Product must not be null and quantity must be positive", exception.getMessage());
    }
}
