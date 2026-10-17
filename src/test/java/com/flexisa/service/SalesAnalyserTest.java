package com.flexisa.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.data.DataLoader;
import com.flexisa.data.ProductParser;
import com.flexisa.model.Product;

class SalesAnalyserTest {

    private List<Product> loadProducts() {
        return DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine).valid();
    }

    private SalesAnalyser createAnalyser() {
        return new SalesAnalyser(loadProducts(), List.of());
    }

    @Test
    void searchProducts_withPartialMatch_returnsMechanicalKeyboards() {
        List<Product> matches = createAnalyser().searchProducts("key");

        assertEquals(List.of("Mechanical Keyboard", "Mechanical Keyboard"),
                matches.stream().map(Product::getName).toList());
    }

    @Test
    void searchProducts_isCaseInsensitive() {
        List<Product> matches = createAnalyser().searchProducts("LAPTOP");

        assertEquals(List.of("P001"), matches.stream().map(Product::getId).toList());
    }

    @Test
    void searchProducts_withKeywordMatchingMultipleProducts_returnsEveryMatch() {
        List<Product> matches = createAnalyser().searchProducts("m");

        assertEquals(List.of("P002", "P003", "P005", "P006", "P007", "P003"),
                matches.stream().map(Product::getId).toList());
    }

    @Test
    void searchProducts_withNoMatch_returnsEmptyList() {
        assertEquals(List.of(), createAnalyser().searchProducts("zzz"));
    }

    @Test
    void searchProducts_withNullOrBlankKeyword_throwsIllegalArgumentException() {
        SalesAnalyser analyser = createAnalyser();

        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
        () -> analyser.searchProducts(null));
IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
        () -> analyser.searchProducts("  "));

        assertEquals("Search keyword must not be null or blank", nullException.getMessage());
        assertEquals("Search keyword must not be null or blank", blankException.getMessage());
        assertEquals(IllegalArgumentException.class,
            assertThrows(IllegalArgumentException.class, () -> analyser.searchProducts("  ")).getClass());
    }

    @Test
    void searchProducts_trimsKeywordBeforeMatching() {
        List<Product> matches = createAnalyser().searchProducts("  laptop  ");

        assertEquals(List.of("P001"), matches.stream().map(Product::getId).toList());
    }
}
