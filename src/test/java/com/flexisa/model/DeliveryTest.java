package com.flexisa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DeliveryTest {
    private final Address address = new Address("12 Allen Ave", "Ikeja", "Lagos", "Nigeria", "100001");
    private final Customer customer = new Customer("customer-1", "Isabelle", "isagoch@gmail.com", address);
    private final Order order = new Order("order-1", customer);
    private final Delivery delivery = new Delivery("delivery-1", order, address);

    @Test
    void newDeliveryStartsPending() {
        assertEquals(DeliveryStatus.PENDING, delivery.getStatus());
    }

    @Test
    void dispatchMovesPendingToInTransit() {
        delivery.dispatch();

        assertEquals(DeliveryStatus.IN_TRANSIT, delivery.getStatus());
    }

    @Test
    void markDeliveredIsRejectedFromPending() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, delivery::markDelivered);

        assertEquals("Delivery can only be marked as delivered when its status is IN_TRANSIT", exception.getMessage());
        assertEquals(DeliveryStatus.PENDING, delivery.getStatus());
    }
}
