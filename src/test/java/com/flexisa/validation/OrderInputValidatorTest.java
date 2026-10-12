package com.flexisa.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderInputValidatorTest {

    @Test
    void validateOrder_WithZeroPrice_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(0, 1, "Lagos"));
        assertEquals("Please input a price greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithNegativePrice_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(-5, 1, "Lagos"));
        assertEquals("Please input a price greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithZeroQuantity_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(100, 0, "Lagos"));
        assertEquals("Please input quantity greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithNegativeQuantity_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(100, -1, "Lagos"));
        assertEquals("Please input quantity greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithNullLocation_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(100, 1, null));
        assertEquals("Please input a valid location", exception.getMessage());
    }

    @Test
    void validateOrder_WithBlankLocation_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(100, 1, "   "));
        assertEquals("Please input a valid location", exception.getMessage());
    }

    @Test
    void validateOrder_WithEmptyLocation_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderInputValidator.validateOrder(100, 1, ""));
        assertEquals("Please input a valid location", exception.getMessage());
    }

    @Test
    void validateOrder_WithValidInput_DoesNotThrow() {
        assertDoesNotThrow(() -> OrderInputValidator.validateOrder(100, 1, "Lagos"));
    }
}