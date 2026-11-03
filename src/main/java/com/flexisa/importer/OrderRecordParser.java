package com.flexisa.importer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.flexisa.model.OrderStatus;

public final class OrderRecordParser {

    private OrderRecordParser() {
    }

    public static ImportedOrder parse(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 8) {
            throw new InvalidRecordException("Wrong number of columns");
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String orderId = requiredField(parts[0], "orderId");
        String customerId = requiredField(parts[1], "customerId");
        String productId = requiredField(parts[2], "productId");

        String quantityText = requiredField(parts[3], "quantity");
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException exception) {
            throw new InvalidRecordException("Quantity is not a whole number");
        }
        if (quantity < 1) {
            throw new InvalidRecordException("Quantity must be at least 1");
        }

        String priceText = requiredField(parts[4], "unitPrice");
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(priceText);
        } catch (NumberFormatException exception) {
            throw new InvalidRecordException("Price is not a number");
        }
        if (!Double.isFinite(unitPrice) || unitPrice <= 0) {
            throw new InvalidRecordException("Price must be greater than 0");
        }

        LocalDate orderDate;
        try {
            orderDate = LocalDate.parse(parts[5]);
        } catch (DateTimeParseException exception) {
            throw new InvalidRecordException("Invalid order date");
        }

        LocalDateTime orderTimestamp;
        try {
            orderTimestamp = LocalDateTime.parse(parts[6]);
        } catch (DateTimeParseException exception) {
            throw new InvalidRecordException("Invalid order timestamp");
        }

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(parts[7].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new InvalidRecordException("Invalid status");
        }

        return new ImportedOrder(orderId, customerId, productId, quantity,
                unitPrice, orderDate, orderTimestamp, status);
    }

    private static String requiredField(String value, String fieldName) {
        if (value.isBlank()) {
            throw new InvalidRecordException("Missing required field: " + fieldName);
        }
        return value;
    }
}
