package com.flexisa.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexisa.exception.DataFileException;

public final class ReportExporter {

    public static final String REPORT_FILE = "import-report.json";
    public static final String REJECTED_FILE = "rejected-records.json";

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private ReportExporter() {
    }

    public static void export(ImportReport report, Path outputDir) throws DataFileException {
        try {
            Files.createDirectories(outputDir);
        } catch (IOException exception) {
            throw new DataFileException("Failed to write report: " + REPORT_FILE, exception);
        }

        try {
            write(outputDir.resolve(REPORT_FILE), report);
        } catch (IOException exception) {
            throw new DataFileException("Failed to write report: " + REPORT_FILE, exception);
        }

        try {
            write(outputDir.resolve(REJECTED_FILE), report.rejectedRecords());
        } catch (IOException exception) {
            throw new DataFileException("Failed to write report: " + REJECTED_FILE, exception);
        }
    }

    private static void write(Path file, Object value) throws IOException {
        try (var writer = Files.newBufferedWriter(file)) {
            MAPPER.writeValue(writer, value);
        }
    }
}
