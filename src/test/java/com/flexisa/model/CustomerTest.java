package com.flexisa.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    private final Address lagos = new Address("12 Allen Ave", "Ikeja", "Lagos", "Nigeria", "100001");

    @Test
    public void validCustomerTest() {
        Customer customer = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);
        assertEquals("2400244", customer.getId());
        assertEquals("Isabelle", customer.getName());
        assertEquals("isagoch@gmail.com", customer.getEmail());
        assertEquals(lagos, customer.getAddress());
    }

    @Test
    public void invalidCustomer_withBlankNameTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Customer("2400244", " ", "isagoch@gmail.com", lagos)
    );
    assertEquals("Customer name must not be blank", exception.getMessage());
    }

    @Test
    public void invalidCustomer_withBadEmail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Customer("2400244", "Isabelle", "isagochgmail.com", lagos)
    );
    assertEquals("Email is incorrect", exception.getMessage());
    }

    @Test
    public void invalidCustomer_withNulAddress() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Customer("2400244", "Isabelle", "isagoch@gmail.com", null)
    );
    assertEquals("Customer address must not be null", exception.getMessage());
    }
}