package com.flexisa.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.flexisa.exception.DataFileException;

public final class OrderImporter {

    private OrderImporter() {
    }

    public static ImportResult importOrders(Path file) throws DataFileException {
        Path name = file.getFileName();
        String fileName = (name == null) ? "input file" : name.toString();
        if (!Files.isRegularFile(file)) {
            throw new DataFileException("Input file not found: " + fileName);
        }

        var accepted = new ArrayList<ImportedOrder>();
        var rejected = new ArrayList<RejectedRecord>();
        Set<String> seenIds = new HashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String header = reader.readLine();
            if (header == null) {
                throw new DataFileException("Input file is empty");
            }

            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                try {
                    ImportedOrder order = OrderRecordParser.parse(line);
                    if (!seenIds.add(order.orderId())) {
                        throw new InvalidRecordException("Duplicate order ID");
                    }
                    accepted.add(order);
                } catch (InvalidRecordException exception) {
                    rejected.add(new RejectedRecord(lineNumber, idOf(line), exception.getMessage()));
                }
            }
        } catch (IOException exception) {
            throw new DataFileException("Failed to read file: " + fileName, exception);
        }

        return new ImportResult(accepted, rejected);
    }

    private static String idOf(String line) {
        return line.split(",", -1)[0].trim();
    }
}
