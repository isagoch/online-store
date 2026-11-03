package com.flexisa.importer;

import java.time.LocalDateTime;
import java.util.List;

public record ImportReport(LocalDateTime processedAt, String inputFile,
        int acceptedCount, int rejectedCount, double totalAmount,
        List<ImportedOrder> acceptedOrders, List<RejectedRecord> rejectedRecords) {

    public ImportReport {
        acceptedOrders = List.copyOf(acceptedOrders);
        rejectedRecords = List.copyOf(rejectedRecords);
    }

    public static ImportReport from(ImportResult result, String inputFile,
            LocalDateTime processedAt) {
        double totalAmount = result.accepted().stream()
                .mapToDouble(ImportedOrder::total)
                .sum();
        return new ImportReport(processedAt, inputFile,
                result.accepted().size(), result.rejected().size(), totalAmount,
                result.accepted(), result.rejected());
    }
}
