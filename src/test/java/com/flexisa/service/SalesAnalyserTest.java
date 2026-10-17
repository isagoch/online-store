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

    @Test
    void filterOrdersByTotalRange_includesExactMinimumAndMaximum() {
        List<Order> minimumMatches = createAnalyser().filterOrdersByTotalRange(12_500, 12_500);
        List<Order> duplicateMaximumMatches = createAnalyser().filterOrdersByTotalRange(35_500, 35_500);

        assertEquals(List.of("O005"), minimumMatches.stream().map(Order::getId).toList());
        assertEquals(List.of("O007", "O007"),
                duplicateMaximumMatches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByTotalRange_includesZeroTotalOrder() {
        List<Order> matches = createAnalyser().filterOrdersByTotalRange(0, 0);

        assertEquals(List.of("O009"), matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByTotalRange_includesCancelledOrders() {
        List<Order> matches = createAnalyser().filterOrdersByTotalRange(1_474_000, 1_474_000);

        assertEquals(List.of("O006"), matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByTotalRange_withMiddleRange_returnsOrdersWithinBounds() {
        List<Order> matches = createAnalyser().filterOrdersByTotalRange(100_000, 220_000);

        assertEquals(List.of("O003", "O004", "O010", "O011", "O012"),
                matches.stream().map(Order::getId).toList());
    }

    @Test
    void filterOrdersByTotalRange_withNoMatches_returnsEmptyList() {
        assertEquals(List.of(), createAnalyser().filterOrdersByTotalRange(500_000, 1_000_000));
    }

    @Test
    void filterOrdersByTotalRange_withMinimumGreaterThanMaximum_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createAnalyser().filterOrdersByTotalRange(10, 5));

        assertEquals("Minimum total must not exceed maximum total", exception.getMessage());
    }

    @Test
    void filterOrdersByTotalRange_withNaNBound_throwsIllegalArgumentException() {
        IllegalArgumentException minimumException = assertThrows(IllegalArgumentException.class,
                () -> createAnalyser().filterOrdersByTotalRange(Double.NaN, 10));
        IllegalArgumentException maximumException = assertThrows(IllegalArgumentException.class,
                () -> createAnalyser().filterOrdersByTotalRange(0, Double.NaN));

        assertEquals("Total range bounds must not be NaN", minimumException.getMessage());
        assertEquals("Total range bounds must not be NaN", maximumException.getMessage());
    }

    @Test
    void sortProductsByPrice_returnsProductsInAscendingPriceOrder() {
        List<Product> sorted = createAnalyser().sortProductsByPrice();

        assertEquals(List.of("P002", "P006", "P004", "P007", "P003", "P003",
                        "P008", "P009", "P005", "P001"),
                sorted.stream().map(Product::getId).toList());
        assertEquals(29_500.0, sorted.get(4).getPrice());
        assertEquals(32_000.0, sorted.get(5).getPrice());
    }

    @Test
    void sortProductsByName_isCaseInsensitiveAndStableForEqualNames() {
        List<Product> sorted = createAnalyser().sortProductsByName();

        assertEquals(List.of("Desk Lamp", "External SSD", "Headphones", "Laptop",
                        "Mechanical Keyboard", "Mechanical Keyboard", "Monitor",
                        "USB-C Hub", "Webcam", "Wireless Mouse"),
                sorted.stream().map(Product::getName).toList());
        List<Double> keyboardPrices = sorted.stream()
                .filter(product -> product.getName().equals("Mechanical Keyboard"))
                .map(Product::getPrice)
                .toList();
        assertEquals(List.of(32_000.0, 29_500.0), keyboardPrices);
    }

    @Test
    void sortProductsByStockQuantity_returnsProductsInAscendingStockOrder() {
        List<Product> sorted = createAnalyser().sortProductsByStockQuantity();

        assertEquals(List.of("P004", "P009", "P005", "P007", "P001",
                        "P008", "P003", "P003", "P006", "P002"),
                sorted.stream().map(Product::getId).toList());
        assertEquals(18, sorted.get(6).getStockQuantity());
        assertEquals(20, sorted.get(7).getStockQuantity());
    }

    @Test
    void sortProducts_withNoProducts_returnsEmptyLists() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(List.of(), analyser.sortProductsByPrice());
        assertEquals(List.of(), analyser.sortProductsByName());
        assertEquals(List.of(), analyser.sortProductsByStockQuantity());
    }

    @Test
    void removeDuplicateProducts_keepsFirstOccurrenceInInsertionOrder() {
        List<Product> uniqueProducts = createAnalyser().removeDuplicateProducts();

        assertEquals(List.of("P001", "P002", "P003", "P004", "P005",
                        "P006", "P007", "P008", "P009"),
                uniqueProducts.stream().map(Product::getId).toList());
        assertEquals(32_000.0, uniqueProducts.get(2).getPrice());
    }

    @Test
    void removeDuplicateProducts_withNoProducts_returnsEmptyList() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(List.of(), analyser.removeDuplicateProducts());
    }

    @Test
    void removeDuplicateProducts_withDistinctIds_returnsProductsUnchanged() {
        List<Product> products = List.of(
                new Product("P100", "Tablet", 90_000, 4),
                new Product("P101", "Smart Watch", 45_000, 7),
                new Product("P102", "Charger", 8_000, 12));
        SalesAnalyser analyser = new SalesAnalyser(products, List.of());

        assertEquals(products, analyser.removeDuplicateProducts());
    }

    @Test
    void removeDuplicateProducts_doesNotChangeAnalyserProductList() {
        SalesAnalyser analyser = createAnalyser();

        analyser.removeDuplicateProducts();

        assertEquals(2, analyser.searchProducts("key").size());
    }
}
