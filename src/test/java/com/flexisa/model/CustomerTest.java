package com.flexisa.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void equals_SameId_ReturnsTrue() {
        Customer first = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);
        Customer second = new Customer("2400244", "Emmanuel", "emmanuel@example.com", lagos);

        assertEquals(first, second);
    }

    @Test
    public void equals_DifferentId_ReturnsFalse() {
        Customer first = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);
        Customer second = new Customer("2400245", "Isabelle", "isagoch@gmail.com", lagos);

        assertNotEquals(first, second);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null_ReturnsFalse() {
        Customer customer = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);

        assertFalse(customer.equals(null));
    }

    @Test
    public void hashCode_SameId_ReturnsSameHashCode() {
        Customer first = new Customer("2400244", "Isabelle", "isagoch@gmail.com", lagos);
        Customer second = new Customer("2400244", "Emmanuel", "emmanuel@example.com", lagos);

        assertEquals(first.hashCode(), second.hashCode());
    }
}