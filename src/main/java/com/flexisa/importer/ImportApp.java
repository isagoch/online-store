package com.flexisa.importer;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.flexisa.exception.ConfigurationException;
import com.flexisa.exception.DataFileException;

public final class ImportApp {

    private ImportApp() {
    }

    public static void main(String[] args) {
        try {
            ImportConfig config = ImportConfig.load(
                    System.getenv(), Path.of("config/importer.properties"));
            ImportResult result = OrderImporter.importOrders(config.inputFile());
            ImportReport report = ImportReport.from(result,
                    config.inputFile().getFileName().toString(),
                    LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            ReportExporter.export(report, config.outputDir());
            System.out.printf("Accepted: %d, Rejected: %d, Total: %.2f%n",
                    report.acceptedCount(), report.rejectedCount(), report.totalAmount());
        } catch (ConfigurationException | DataFileException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
