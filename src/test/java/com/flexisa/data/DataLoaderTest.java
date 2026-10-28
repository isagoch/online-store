package com.flexisa.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.flexisa.exception.DataFileException;
import com.flexisa.model.Customer;
import com.flexisa.model.Product;

class DataLoaderTest {

    @TempDir
    private Path tempDir;

    @Test
    void readFileLines_ValidFile_SkipsHeaderAndBlankLines() throws DataFileException, IOException {
        Path file = tempDir.resolve("valid.csv");
        Files.writeString(file, "id,name\nA,1\n\nB,2\n");

        assertEquals(List.of("A,1", "B,2"), DataLoader.readFileLines(file));
    }

    @Test
    void readFileLines_HeaderOnlyFile_ReturnsEmptyList() throws DataFileException, IOException {
        Path file = tempDir.resolve("header-only.csv");
        Files.writeString(file, "id,name\n");

        assertTrue(DataLoader.readFileLines(file).isEmpty());
    }

    @Test
    void readFileLines_MissingFile_ThrowsDataFileException() throws DataFileException {
        DataFileException exception = assertThrows(
                DataFileException.class,
                () -> DataLoader.readFileLines(tempDir.resolve("missing.csv")));

        assertEquals("Failed to read file: missing.csv", exception.getMessage());
        assertTrue(exception.getCause() instanceof NoSuchFileException);
    }

    @Test
    void readFileLines_MissingFile_LogDoesNotContainFullPath() throws DataFileException {
        Logger logger = Logger.getLogger(DataLoader.class.getName());
        List<LogRecord> records = new ArrayList<>();
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                records.add(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        logger.addHandler(handler);
        try {
            DataFileException exception = assertThrows(
                    DataFileException.class,
                    () -> DataLoader.readFileLines(tempDir.resolve("missing.csv")));
            assertEquals("Failed to read file: missing.csv", exception.getMessage());
        } finally {
            logger.removeHandler(handler);
        }

        assertFalse(records.isEmpty());
        LogRecord record = records.get(0);
        assertTrue(record.getMessage().contains("missing.csv"));
        assertFalse(record.getMessage().contains(tempDir.toString()));
        assertNull(record.getThrown());
    }

    @Test
    void readFileLines_NullPath_ThrowsIllegalArgumentException() throws DataFileException {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DataLoader.readFileLines(null));

        assertEquals("Path must not be null", exception.getMessage());
    }

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
