package com.flexisa.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AddressTest {
    @Test
    public void validAddressTest() {
        Address address = new Address("123 Main St", "Springfield", "IL", "USA", "62701");
        assertEquals("123 Main St", address.street());
        assertEquals("Springfield", address.city());
        assertEquals("IL", address.state());
        assertEquals("USA", address.country());
        assertEquals("62701", address.postalCode());
    }

    @Test
    public void invalidAddress_withNullTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new Address(null, "Springfield", "IL", "USA", "62701")
        );
        assertEquals("street must not be blank", exception.getMessage());
    }

    @Test
    public void invalidAddress_withBlankTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Address("   ", "Springfield", "IL", "USA", "62701")
        );
        assertEquals("street must not be blank", exception.getMessage());
    }
}
