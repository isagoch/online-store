package com.flexisa.importer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flexisa.model.OrderStatus;

public record ImportedOrder(String orderId, String customerId, String productId,
        int quantity, double unitPrice, LocalDate orderDate,
        LocalDateTime orderTimestamp, OrderStatus status) {

    public double total() {
        return quantity * unitPrice;
    }
}
