package com.flexisa.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.flexisa.exception.DataFileException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderImporterTest {

    @TempDir
    private Path tempDir;

    @Test
    void importOrders_parsesSampleAndRejectsEachInvalidRow() throws DataFileException {
        ImportResult result = OrderImporter.importOrders(
                Path.of("data", "sample-orders.csv"));

        assertEquals(20, result.accepted().size());
        assertEquals(7, result.rejected().size());
        assertEquals(1_032_500.00,
                result.accepted().stream().mapToDouble(ImportedOrder::total).sum(), 0.0);
        assertEquals(List.of(
                "Invalid order date",
                "Missing required field: quantity",
                "Quantity must be at least 1",
                "Invalid status",
                "Duplicate order ID",
                "Price is not a number",
                "Invalid order timestamp"),
                result.rejected().stream().map(RejectedRecord::reason).toList());
    }

    @Test
    void importOrders_missingFileUsesOnlyFileNameInError() {
        Path missingFile = tempDir.resolve("missing.csv");

        DataFileException exception = assertThrows(
                DataFileException.class,
                () -> OrderImporter.importOrders(missingFile));

        assertEquals("Input file not found: missing.csv", exception.getMessage());
        assertFalse(exception.getMessage().contains(tempDir.toString()));
    }

    @Test
    void importOrders_rootPathWithoutFileNameUsesFallbackInError() {
        Path root = Path.of(tempDir.getFileSystem().getSeparator());

        DataFileException exception = assertThrows(
                DataFileException.class,
                () -> OrderImporter.importOrders(root));

        assertEquals("Input file not found: input file", exception.getMessage());
    }

    @Test
    void importOrders_emptyFileThrowsButHeaderOnlyFileIsValid()
            throws IOException, DataFileException {
        Path emptyFile = tempDir.resolve("empty.csv");
        Files.writeString(emptyFile, "");

        DataFileException exception = assertThrows(
                DataFileException.class,
                () -> OrderImporter.importOrders(emptyFile));
        assertEquals("Input file is empty", exception.getMessage());

        Path headerOnlyFile = tempDir.resolve("header-only.csv");
        Files.writeString(headerOnlyFile,
                "orderId,customerId,productId,quantity,unitPrice,orderDate,orderTimestamp,status\n");

        ImportResult result = OrderImporter.importOrders(headerOnlyFile);
        assertTrue(result.accepted().isEmpty());
        assertTrue(result.rejected().isEmpty());
    }

    @Test
    void importOrders_skipsBlankLinesAndRetainsPhysicalLineNumbers()
            throws IOException, DataFileException {
        Path file = tempDir.resolve("blank-lines.csv");
        Files.writeString(file,
                "header\n\nO1001,C001,P001,0,15000,2025-03-01,2025-03-01T09:15:00,PENDING\n");

        ImportResult result = OrderImporter.importOrders(file);

        assertTrue(result.accepted().isEmpty());
        assertEquals(1, result.rejected().size());
        assertEquals(3, result.rejected().get(0).lineNumber());
    }

    @Test
    void importOrders_rejectedRowsDoNotReserveDuplicateIds()
            throws IOException, DataFileException {
        Path file = tempDir.resolve("rejected-then-valid.csv");
        Files.writeString(file,
                """
                header
                O1001,C001,P001,0,15000,2025-03-01,2025-03-01T09:15:00,PENDING
                O1001,C001,P001,2,15000,2025-03-01,2025-03-01T09:15:00,PENDING
                """);

        ImportResult result = OrderImporter.importOrders(file);

        assertEquals(1, result.accepted().size());
        assertEquals(1, result.rejected().size());
        assertEquals("Quantity must be at least 1", result.rejected().get(0).reason());
    }

    @Test
    void importResultCopiesLists() {
        var accepted = new java.util.ArrayList<ImportedOrder>();
        var rejected = new java.util.ArrayList<RejectedRecord>();

        ImportResult result = new ImportResult(accepted, rejected);
        accepted.clear();
        rejected.clear();

        assertTrue(result.accepted().isEmpty());
        assertTrue(result.rejected().isEmpty());
        assertEquals(UnsupportedOperationException.class,
                assertThrows(UnsupportedOperationException.class,
                        () -> result.accepted().add(null)).getClass());
    }
}
