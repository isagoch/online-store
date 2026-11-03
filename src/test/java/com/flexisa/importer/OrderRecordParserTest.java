package com.flexisa.importer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.model.OrderStatus;

class OrderRecordParserTest {

    private static final String VALID_LINE =
            "O1001,C001,P001,2,15000.00,2025-03-01,2025-03-01T09:15:00,PENDING";

    @Test
    void parse_returnsTypedOrderAndCalculatesTotal() {
        ImportedOrder order = OrderRecordParser.parse(VALID_LINE);

        assertEquals(new ImportedOrder(
                "O1001", "C001", "P001", 2, 15000.00,
                LocalDate.of(2025, 3, 1),
                LocalDateTime.of(2025, 3, 1, 9, 15),
                OrderStatus.PENDING), order);
        assertEquals(30000.00, order.total(), 0.0);
    }

    @Test
    void parse_trimsValuesAndAcceptsCaseInsensitiveStatus() {
        ImportedOrder order = OrderRecordParser.parse(
                " O1001 , C001 , P001 , 2 , 15000.00 , 2025-03-01 , 2025-03-01T09:15:00 , pending ");

        assertEquals("O1001", order.orderId());
        assertEquals(OrderStatus.PENDING, order.status());
    }

    @Test
    void parse_rejectsIncorrectColumnCountIncludingTrailingEmptyColumn() {
        assertInvalid("Wrong number of columns", "O1001,C001,P001,2,15000,2025-03-01,2025-03-01T09:15:00");
        assertInvalid("Wrong number of columns", VALID_LINE + ",");
    }

    @Test
    void parse_rejectsMissingRequiredFields() {
        assertInvalid("Missing required field: orderId",
                ",C001,P001,2,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Missing required field: customerId",
                "O1001,,P001,2,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Missing required field: productId",
                "O1001,C001,,2,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Missing required field: quantity",
                "O1001,C001,P001,,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Missing required field: unitPrice",
                "O1001,C001,P001,2,,2025-03-01,2025-03-01T09:15:00,PENDING");
    }

    @Test
	void parse_validatesQuantity() {
        assertInvalid("Quantity is not a whole number",
                "O1001,C001,P001,abc,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Quantity must be at least 1",
                "O1001,C001,P001,0,15000,2025-03-01,2025-03-01T09:15:00,PENDING");
    }

    @Test
    void parse_validatesPrice() {
        assertInvalid("Price is not a number",
                "O1001,C001,P001,2,abc,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Price must be greater than 0",
                "O1001,C001,P001,2,0,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Price must be greater than 0",
                "O1001,C001,P001,2,-1,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Price must be greater than 0",
                "O1001,C001,P001,2,NaN,2025-03-01,2025-03-01T09:15:00,PENDING");
        assertInvalid("Price must be greater than 0",
                "O1001,C001,P001,2,Infinity,2025-03-01,2025-03-01T09:15:00,PENDING");
    }

    @Test
    void parse_validatesDateTimestampAndStatus() {
        assertInvalid("Invalid order date",
                "O1001,C001,P001,2,15000,2025-13-45,2025-03-01T09:15:00,PENDING");
        assertInvalid("Invalid order timestamp",
                "O1001,C001,P001,2,15000,2025-03-01,not-a-time,PENDING");
        assertInvalid("Invalid status",
                "O1001,C001,P001,2,15000,2025-03-01,2025-03-01T09:15:00,REFUNDED");
    }

    private static void assertInvalid(String reason, String line) {
        InvalidRecordException exception = assertThrows(
                InvalidRecordException.class,
                () -> OrderRecordParser.parse(line));
        assertEquals(reason, exception.getMessage());
    }
}
