package com.flexisa.importer;

public record RejectedRecord(int lineNumber, String orderId, String reason) {
}
