package com.flexisa.model;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    public void validProductTest() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        assertEquals("P001", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
        assertEquals(10, product.getStockQuantity());
    }

    @Test
    public void invalidProduct_withBlankIDTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Product("   ", "Laptop", 999.99, 10)
        );
        assertEquals("Product ID must not be blank", exception.getMessage());
    }

    @Test
    public void invalidProduct_withNegativePriceTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Product("P001", "Laptop", -999.99, 10)
        );
        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    public void invalidProduct_withNegativeStockQuantityTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Product("P001", "Laptop", 999.99, -10)
        );
        assertEquals("Stock quantity must not be negative", exception.getMessage());
    }

    @Test
    public void reduceStock_correctlyTest() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        product.reduceStock(5);
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    public void reduceStock_withZeroQuantityTest() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            product.reduceStock(0)
        );
        assertEquals("Quantity to reduce must be positive", exception.getMessage());
    }

    @Test
    public void reduceStock_withInsufficientStockTest() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            product.reduceStock(15)
        );
        assertEquals("Insufficient stock to reduce by 15", exception.getMessage());
    }

    @Test
    public void increaseStock_correctlyTest() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        product.increaseStock(5);
        assertEquals(15, product.getStockQuantity());
    }

    @Test
    public void equals_SameId_ReturnsTrue() {
        Product first = new Product("P001", "Laptop", 999.99, 10);
        Product second = new Product("P001", "Phone", 499.99, 5);

        assertEquals(first, second);
    }

    @Test
    public void equals_DifferentId_ReturnsFalse() {
        Product first = new Product("P001", "Laptop", 999.99, 10);
        Product second = new Product("P002", "Laptop", 999.99, 10);

        assertNotEquals(first, second);
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null_ReturnsFalse() {
        Product product = new Product("P001", "Laptop", 999.99, 10);

        assertFalse(product.equals(null));
    }

    @Test
    public void hashCode_SameId_ReturnsSameHashCode() {
        Product first = new Product("P001", "Laptop", 999.99, 10);
        Product second = new Product("P001", "Phone", 499.99, 5);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void hashSet_ProductChangesStockAfterAdding_StillFound() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        Set<Product> products = new HashSet<>();
        products.add(product);

        product.reduceStock(1);

        assertTrue(products.contains(product));
    }
}
