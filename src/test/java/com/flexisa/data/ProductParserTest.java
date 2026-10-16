package com.flexisa.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.model.Product;

public class ProductParserTest {

    @Test
    void parseLine_withValidValues_returnsProductWithExpectedFields() {
        Product product = ProductParser.parseLine("P001,Laptop,450000,12");

        assertEquals("P001", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(450000.0, product.getPrice());
        assertEquals(12, product.getStockQuantity());
    }

    @Test
    void parseLine_withSpacesAroundValues_trimsValues() {
        Product product = ProductParser.parseLine(" P001 , Laptop , 450000 , 12 ");

        assertEquals("P001", product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(450000.0, product.getPrice());
        assertEquals(12, product.getStockQuantity());
    }

    @Test
    void parseLine_withTooFewColumns_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProductParser.parseLine("P001,Laptop,450000"));

        assertEquals("Invalid product line: P001,Laptop,450000", exception.getMessage());
    }

    @Test
    void parseLine_withNonNumericPrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductParser.parseLine("P001,Laptop,abc,12"));
    }

    @Test
    void parseLine_withNullOrBlankLine_throwsIllegalArgumentException() {
        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
                () -> ProductParser.parseLine(null));
        IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
                () -> ProductParser.parseLine("   "));

        assertEquals("Product line must not be blank", nullException.getMessage());
        assertEquals("Product line must not be blank", blankException.getMessage());
    }

    @Test
    void parseLine_withNegativePrice_propagatesProductValidationMessage() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ProductParser.parseLine("P001,Laptop,-450000,12"));

        assertEquals("Price must be greater than zero", exception.getMessage());
    }
}
