package com.flexisa.importer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.flexisa.model.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportReportTest {

    @Test
    void from_calculatesCountsTotalAndPreservesDetails() {
        ImportedOrder order = new ImportedOrder(
                "O1001", "C001", "P001", 2, 15000.0,
                LocalDate.of(2025, 3, 1),
                LocalDateTime.of(2025, 3, 1, 9, 15),
                OrderStatus.PENDING);
        RejectedRecord rejected = new RejectedRecord(22, "O1021", "Invalid order date");
        ImportResult result = new ImportResult(List.of(order), List.of(rejected));
        LocalDateTime processedAt = LocalDateTime.of(2025, 3, 15, 12, 30);

        ImportReport report = ImportReport.from(result, "sample-orders.csv", processedAt);

        assertEquals(processedAt, report.processedAt());
        assertEquals("sample-orders.csv", report.inputFile());
        assertEquals(1, report.acceptedCount());
        assertEquals(1, report.rejectedCount());
        assertEquals(30000.0, report.totalAmount(), 0.0);
        assertEquals(List.of(order), report.acceptedOrders());
        assertEquals(List.of(rejected), report.rejectedRecords());
    }

    @Test
    void reportCopiesListsAndPreventsMutation() {
        var accepted = new ArrayList<ImportedOrder>();
        var rejected = new ArrayList<RejectedRecord>();
        ImportReport report = new ImportReport(
                LocalDateTime.now(), "orders.csv", 0, 0, 0, accepted, rejected);

        accepted.add(null);
        rejected.add(null);

        assertTrue(report.acceptedOrders().isEmpty());
        assertTrue(report.rejectedRecords().isEmpty());
        assertEquals(UnsupportedOperationException.class,
            assertThrows(UnsupportedOperationException.class,
                () -> report.acceptedOrders().add(null)).getClass());
    }
}
