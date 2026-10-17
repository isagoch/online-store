package com.flexisa.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.data.CustomerParser;
import com.flexisa.data.DataLoader;
import com.flexisa.data.ProductParser;
import com.flexisa.data.SampleOrders;
import com.flexisa.model.Customer;
import com.flexisa.model.Order;
import com.flexisa.model.OrderStatus;
import com.flexisa.model.Product;

class SalesAnalyserTest {

    private List<Product> loadProducts() {
        return DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine).valid();
    }

    private List<Customer> loadCustomers() {
        return DataLoader.parseAll(
                DataLoader.readResourceLines("customers.csv"),
                CustomerParser::parseLine).valid();
    }

    private SalesAnalyser createAnalyser() {
        List<Product> products = loadProducts();
        List<Order> orders = SampleOrders.create(loadCustomers(), products);
        return new SalesAnalyser(products, orders);
    }

    @Test
    void searchProducts_withPartialMatch_returnsMechanicalKeyboards() {
        List<Product> matches = createAnalyser().searchProducts("key");

        assertEquals(List.of("Mechanical Keyboard", "Mechanical Keyboard"),
                matches.stream().map(Product::getName).toList());
    }

    @Test
    void searchProducts_isCaseInsensitive() {
        List<Product> matches = createAnalyser().searchProducts("LAPTOP");

        assertEquals(List.of("P001"), matches.stream().map(Product::getId).toList());
    }

    @Test
    void searchProducts_withKeywordMatchingMultipleProducts_returnsEveryMatch() {
        List<Product> matches = createAnalyser().searchProducts("m");

        assertEquals(List.of("P002", "P003", "P005", "P006", "P007", "P003"),
                matches.stream().map(Product::getId).toList());
    }

    @Test
    void searchProducts_withNoMatch_returnsEmptyList() {
        assertEquals(List.of(), createAnalyser().searchProducts("zzz"));
    }

    @Test
    void searchProducts_withNullOrBlankKeyword_throwsIllegalArgumentException() {
        SalesAnalyser analyser = createAnalyser();

        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
        () -> analyser.searchProducts(null));
        IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
        () -> analyser.searchProducts("  "));

        assertEquals("Search keyword must not be null or blank", nullException.getMessage());
        assertEquals("Search keyword must not be null or blank", blankException.getMessage());
    }

    @Test
    void searchProducts_trimsKeywordBeforeMatching() {
        List<Product> matches = createAnalyser().searchProducts("  laptop  ");

        assertEquals(List.of("P001"), matches.stream().map(Product::getId).toList());
    }

    @Test
    void searchOrders_withDuplicateOrderId_returnsBothRecords() {
        List<Order> matches = createAnalyser().searchOrders("O007");

        assertEquals(List.of("O007", "O007"), matches.stream().map(Order::getId).toList());
    }

    @Test
    void searchOrders_withCustomerName_returnsMatchingOrdersInDatasetOrder() {
        List<Order> matches = createAnalyser().searchOrders("ada");

        assertEquals(List.of("O001", "O002", "O003", "O011"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void searchOrders_isCaseInsensitiveForCustomerName() {
        List<Order> matches = createAnalyser().searchOrders("CHIKA");

        assertEquals(List.of("O006"), matches.stream().map(Order::getId).toList());
    }

    @Test
    void searchOrders_withNoMatch_returnsEmptyList() {
        assertEquals(List.of(), createAnalyser().searchOrders("zzz"));
    }

    @Test
    void searchOrders_withNullOrBlankKeyword_throwsIllegalArgumentException() {
        SalesAnalyser analyser = createAnalyser();

        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
                () -> analyser.searchOrders(null));
        IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
                () -> analyser.searchOrders("  "));

        assertEquals("Search keyword must not be null or blank", nullException.getMessage());
        assertEquals("Search keyword must not be null or blank", blankException.getMessage());
    }

    @Test
    void filterOrdersByStatus_withDeliveredStatus_returnsDeliveredOrders() {
        List<Order> matches = createAnalyser().filterOrdersByStatus(OrderStatus.DELIVERED);

        assertEquals(List.of("O001", "O004", "O010"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByStatus_withShippedStatus_keepsDuplicateOrders() {
        List<Order> matches = createAnalyser().filterOrdersByStatus(OrderStatus.SHIPPED);

        assertEquals(List.of("O002", "O007", "O007"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByStatus_withNullStatus_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createAnalyser().filterOrdersByStatus(null));

        assertEquals("Order status must not be null", exception.getMessage());
    }

    @Test
    void filterOrdersByStatus_withNoOrders_returnsEmptyList() {
        SalesAnalyser analyser = new SalesAnalyser(loadProducts(), List.of());

        assertEquals(List.of(), analyser.filterOrdersByStatus(OrderStatus.DELIVERED));
    }

    @Test
    void filterOrdersByCustomerId_withC001_returnsItsOrdersInDatasetOrder() {
        List<Order> matches = createAnalyser().filterOrdersByCustomerId("C001");

        assertEquals(List.of("O001", "O002", "O003", "O011"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByCustomerId_withC004_keepsDuplicateOrderRecords() {
        List<Order> matches = createAnalyser().filterOrdersByCustomerId("C004");

        assertEquals(List.of("O007", "O007", "O012"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByCustomerId_withCustomerWhoHasNoOrders_returnsEmptyList() {
        assertEquals(List.of(), createAnalyser().filterOrdersByCustomerId("C007"));
    }

    @Test
    void filterOrdersByCustomerId_withNullOrBlankId_throwsIllegalArgumentException() {
        SalesAnalyser analyser = createAnalyser();

        IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class,
                () -> analyser.filterOrdersByCustomerId(null));
        IllegalArgumentException blankException = assertThrows(IllegalArgumentException.class,
                () -> analyser.filterOrdersByCustomerId("  "));

        assertEquals("Customer ID must not be null or blank", nullException.getMessage());
        assertEquals("Customer ID must not be null or blank", blankException.getMessage());
    }

    @Test
    void filterOrdersByCustomerId_trimsIdBeforeMatching() {
        List<Order> matches = createAnalyser().filterOrdersByCustomerId("  C001  ");

        assertEquals(List.of("O001", "O002", "O003", "O011"),
                matches.stream().map(Order::getId).toList());
    }
}
