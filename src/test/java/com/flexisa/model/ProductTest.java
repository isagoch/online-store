package com.flexisa.model;
import static org.junit.jupiter.api.Assertions.*;
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
}
