package com.flexisa.data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.flexisa.model.Customer;
import com.flexisa.model.Product;

class DataLoaderTest {

    @Test
    void readResourceLines_skipsHeaderAndBlankLines() {
        List<String> lines = DataLoader.readResourceLines("com/flexisa/data/data-loader-lines.csv");

        assertEquals(List.of("first row", "second row"), lines);
    }

    @Test
    void readResourceLines_withMissingResource_throwsExceptionContainingResourceName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DataLoader.readResourceLines("missing.csv"));

        assertTrue(exception.getMessage().contains("missing.csv"));
    }

    @Test
    void parseAll_withAllValidProductLines_returnsAllProductsWithoutErrors() {
        LoadResult<Product> result = DataLoader.parseAll(
                List.of("P001,Laptop,450000,12", "P002,Mouse,15000,20"),
                ProductParser::parseLine);

        assertEquals(2, result.valid().size());
        assertEquals("Laptop", result.valid().get(0).getName());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void parseAll_withInvalidLine_keepsValidRowsAndReportsLineNumber() {
        LoadResult<Product> result = DataLoader.parseAll(
                List.of("P001,Laptop,450000,12", "P002,Mouse,invalid,20", "P003,Keyboard,25000,8"),
                ProductParser::parseLine);

        assertEquals(2, result.valid().size());
        assertEquals(1, result.errors().size());
        assertTrue(result.errors().get(0).contains("Line 2"));
        assertTrue(result.errors().get(0).contains("invalid"));
    }

    @Test
    void parseAll_withNoLines_returnsEmptyLists() {
        LoadResult<Product> result = DataLoader.parseAll(List.of(), ProductParser::parseLine);

        assertTrue(result.valid().isEmpty());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void parseAll_withBlankLine_recordsAnError() {
        LoadResult<Product> result = DataLoader.parseAll(List.of(""), ProductParser::parseLine);

        assertEquals(0, result.valid().size());
        assertEquals(1, result.errors().size());
    }

    @Test
    void parseAll_withCustomerParser_returnsCustomers() {
        LoadResult<Customer> result = DataLoader.parseAll(
                List.of("C001,Ada Okafor,ada@example.com,14 Allen Avenue,Ikeja,Lagos,Nigeria,00100"),
                CustomerParser::parseLine);

        assertEquals(1, result.valid().size());
        assertEquals("Ada Okafor", result.valid().get(0).getName());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    void parseAll_withProductResource_matchesDatasetCountsAndErrorLines() {
        LoadResult<Product> result = DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine);

        assertEquals(10, result.valid().size());
        assertEquals(3, result.errors().size());
        assertTrue(result.errors().get(0).contains("Line 11"));
        assertTrue(result.errors().get(1).contains("Line 12"));
        assertTrue(result.errors().get(2).contains("Line 13"));
    }

    @Test
    void parseAll_withCustomerResource_matchesDatasetCounts() {
        LoadResult<Customer> result = DataLoader.parseAll(
                DataLoader.readResourceLines("customers.csv"),
                CustomerParser::parseLine);

        assertEquals(8, result.valid().size());
        assertEquals(3, result.errors().size());
    }
}
