package com.flexisa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PaymentTest {
    private final Address address = new Address("12 Allen Ave", "Ikeja", "Lagos", "Nigeria", "100001");
    private final Customer customer = new Customer("customer-1", "Isabelle", "isagoch@gmail.com", address);
    private final Order order = new Order("order-1", customer);
    private final Payment payment = new Payment("payment-1", order, 10.0);

    @Test
    void newPaymentStartsPending() {
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void completeMovesPendingToCompleted() {
        payment.complete();

        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    void refundIsRejectedWhenNotCompleted() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, payment::refund);

        assertEquals("Payment can only be refunded when its status is COMPLETED", exception.getMessage());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }
}
