package com.flexisa.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexisa.exception.DataFileException;
import com.flexisa.model.OrderStatus;

class ReportExporterTest {

    @TempDir
    Path tempDir;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void export_writesFullReportAndRejectedRecordsAsSeparateJsonFiles()
            throws DataFileException, IOException {
        ImportedOrder order = new ImportedOrder(
                "O1001", "C001", "P001", 2, 15000.0,
                LocalDate.of(2025, 3, 1),
                LocalDateTime.of(2025, 3, 1, 9, 15),
                OrderStatus.PENDING);
        RejectedRecord rejected = new RejectedRecord(22, "O1021", "Invalid order date");
        ImportReport report = ImportReport.from(
                new ImportResult(List.of(order), List.of(rejected)),
                "sample-orders.csv",
                LocalDateTime.of(2025, 3, 15, 12, 30));
        Path outputDir = tempDir.resolve("nested").resolve("output");

        ReportExporter.export(report, outputDir);

        JsonNode fullReport = mapper.readTree(outputDir.resolve(ReportExporter.REPORT_FILE).toFile());
        assertEquals("sample-orders.csv", fullReport.get("inputFile").asText());
        assertEquals(1, fullReport.get("acceptedCount").asInt());
        assertEquals(1, fullReport.get("rejectedCount").asInt());
        assertEquals(30000.0, fullReport.get("totalAmount").asDouble(), 0.0);
        assertEquals("2025-03-15T12:30:00", fullReport.get("processedAt").asText());
        assertEquals("2025-03-01", fullReport.get("acceptedOrders").get(0).get("orderDate").asText());
        assertEquals("2025-03-01T09:15:00",
                fullReport.get("acceptedOrders").get(0).get("orderTimestamp").asText());
        assertEquals(30000.0, fullReport.get("acceptedOrders").get(0).get("total").asDouble(), 0.0);

        JsonNode rejectedReport = mapper.readTree(
                outputDir.resolve(ReportExporter.REJECTED_FILE).toFile());
        assertTrue(rejectedReport.isArray());
        assertEquals(1, rejectedReport.size());
        assertEquals(22, rejectedReport.get(0).get("lineNumber").asInt());
        assertEquals("O1021", rejectedReport.get(0).get("orderId").asText());
        assertEquals("Invalid order date", rejectedReport.get(0).get("reason").asText());
        assertFalse(rejectedReport.get(0).has("unitPrice"));
    }

    @Test
    void export_reportsDirectoryCreationFailureWithoutExposingOutputPath()
            throws IOException {
        Path outputPath = tempDir.resolve("not-a-directory");
        Files.writeString(outputPath, "file");
        ImportReport report = ImportReport.from(
                new ImportResult(List.of(), List.of()), "orders.csv", LocalDateTime.now());

        DataFileException exception = assertThrows(
                DataFileException.class,
                () -> ReportExporter.export(report, outputPath));

        assertEquals("Failed to write report: " + ReportExporter.REPORT_FILE,
                exception.getMessage());
        assertFalse(exception.getMessage().contains(tempDir.toString()));
    }
}
