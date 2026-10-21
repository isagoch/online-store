package com.flexisa.service;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.flexisa.data.CustomerParser;
import com.flexisa.data.DataLoader;
import com.flexisa.data.ProductParser;
import com.flexisa.data.SampleOrders;
import com.flexisa.model.Customer;
import com.flexisa.model.Order;
import com.flexisa.model.OrderItem;
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
    void calculateTotalSales_withSampleData_sumsUniqueNonCancelledItemTotals() {
        assertEquals(1_340_500.0, createAnalyser().calculateTotalSales());
    }

    @Test
    void calculateTotalSales_withNoOrders_returnsZero() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(0.0, analyser.calculateTotalSales());
    }

    @Test
    void calculateTotalSales_withOnlyCancelledOrders_returnsZero() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 250.0, 1);
        Order cancelledOrder = new Order("CANCELLED", customer);
        cancelledOrder.addItem(new OrderItem(product, 2));
        cancelledOrder.cancel();
        SalesAnalyser analyser = new SalesAnalyser(List.of(product), List.of(cancelledOrder));

        assertEquals(0.0, analyser.calculateTotalSales());
    }

    @Test
    void calculateTotalSales_withHandBuiltOrders_sumsTheirItemTotals() {
        Customer customer = loadCustomers().get(0);
        Product firstProduct = new Product("P100", "First Product", 10_000.0, 2);
        Product secondProduct = new Product("P101", "Second Product", 3_000.0, 4);
        Order firstOrder = new Order("FIRST", customer);
        firstOrder.addItem(new OrderItem(firstProduct, 2));
        Order secondOrder = new Order("SECOND", customer);
        secondOrder.addItem(new OrderItem(secondProduct, 4));
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(firstProduct, secondProduct), List.of(firstOrder, secondOrder));

        assertEquals(32_000.0, analyser.calculateTotalSales());
    }

    @Test
    void calculateTotalSales_withDuplicateOrderId_countsOnlyFirstOrder() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 250.0, 2);
        Order firstOrder = new Order("DUPLICATE", customer);
        firstOrder.addItem(new OrderItem(product, 2));
        Order duplicateOrder = new Order("DUPLICATE", customer);
        duplicateOrder.addItem(new OrderItem(product, 5));
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(product), List.of(firstOrder, duplicateOrder));

        assertEquals(500.0, analyser.calculateTotalSales());
    }

    @Test
    void calculateAverageOrderValue_withSampleData_returnsAverageForUniqueNonCancelledOrders() {
        assertEquals(134_050.0, createAnalyser().calculateAverageOrderValue());
    }

    @Test
    void calculateAverageOrderValue_withNoOrders_returnsZero() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(0.0, analyser.calculateAverageOrderValue());
    }

    @Test
    void calculateAverageOrderValue_withOnlyCancelledOrders_returnsZero() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 250.0, 1);
        Order cancelledOrder = new Order("CANCELLED", customer);
        cancelledOrder.addItem(new OrderItem(product, 2));
        cancelledOrder.cancel();
        SalesAnalyser analyser = new SalesAnalyser(List.of(product), List.of(cancelledOrder));

        assertEquals(0.0, analyser.calculateAverageOrderValue());
    }

    @Test
    void calculateAverageOrderValue_withHandBuiltOrders_returnsMeanItemTotal() {
        Customer customer = loadCustomers().get(0);
        Product firstProduct = new Product("P100", "First Product", 10_000.0, 2);
        Product secondProduct = new Product("P101", "Second Product", 3_000.0, 4);
        Order firstOrder = new Order("FIRST", customer);
        firstOrder.addItem(new OrderItem(firstProduct, 2));
        Order secondOrder = new Order("SECOND", customer);
        secondOrder.addItem(new OrderItem(secondProduct, 4));
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(firstProduct, secondProduct), List.of(firstOrder, secondOrder));

        assertEquals(16_000.0, analyser.calculateAverageOrderValue());
    }

    @Test
    void calculateAverageOrderValue_countsEmptyOrderAsZeroValueOrder() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 20_000.0, 1);
        Order valuedOrder = new Order("VALUED", customer);
        valuedOrder.addItem(new OrderItem(product, 1));
        Order emptyOrder = new Order("EMPTY", customer);
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(product), List.of(valuedOrder, emptyOrder));

        assertEquals(10_000.0, analyser.calculateAverageOrderValue());
    }

    @Test
    void calculateAverageOrderValue_withDuplicateOrderId_countsOnlyFirstOrder() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 250.0, 5);
        Order firstOrder = new Order("DUPLICATE", customer);
        firstOrder.addItem(new OrderItem(product, 2));
        Order duplicateOrder = new Order("DUPLICATE", customer);
        duplicateOrder.addItem(new OrderItem(product, 5));
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(product), List.of(firstOrder, duplicateOrder));

        assertEquals(500.0, analyser.calculateAverageOrderValue());
    }

    @Test
    void findBestSellingProducts_withLimitThree_returnsTopProductsByUnits() {
        List<ProductSales> bestSelling = createAnalyser().findBestSellingProducts(3);

        assertEquals(List.of("P002", "P006", "P003"),
                bestSelling.stream().map(sales -> sales.product().getId()).toList());
        assertEquals(List.of(10, 4, 3),
                bestSelling.stream().map(ProductSales::unitsSold).toList());
    }

    @Test
    void findBestSellingProducts_withLimitFive_breaksUnitTiesByProductId() {
        List<ProductSales> bestSelling = createAnalyser().findBestSellingProducts(5);

        assertEquals(List.of("P002", "P006", "P003", "P007", "P008"),
                bestSelling.stream().map(sales -> sales.product().getId()).toList());
        assertEquals(List.of(10, 4, 3, 3, 3),
                bestSelling.stream().map(ProductSales::unitsSold).toList());
    }

    @Test
    void findBestSellingProducts_withLimitAboveSoldProductCount_returnsAllProducts() {
        List<ProductSales> bestSelling = createAnalyser().findBestSellingProducts(20);

        assertEquals(9, bestSelling.size());
        assertEquals(28, bestSelling.stream().mapToInt(ProductSales::unitsSold).sum());
    }

    @Test
    void findBestSellingProducts_withNoOrders_returnsEmptyList() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(List.of(), analyser.findBestSellingProducts(3));
    }

    @Test
    void findBestSellingProducts_excludesProductSoldOnlyInCancelledOrder() {
        Customer customer = loadCustomers().get(0);
        Product cancelledProduct = new Product("CANCELLED-PRODUCT", "Cancelled Product", 250.0, 1);
        Order cancelledOrder = new Order("CANCELLED", customer);
        cancelledOrder.addItem(new OrderItem(cancelledProduct, 1));
        cancelledOrder.cancel();
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(cancelledProduct), List.of(cancelledOrder));

        assertEquals(List.of(), analyser.findBestSellingProducts(3));
    }

    @Test
    void findBestSellingProducts_withDuplicateOrderId_countsOnlyFirstOrder() {
        Customer customer = loadCustomers().get(0);
        Product product = new Product("P100", "Test Product", 250.0, 5);
        Order firstOrder = new Order("DUPLICATE", customer);
        firstOrder.addItem(new OrderItem(product, 2));
        Order duplicateOrder = new Order("DUPLICATE", customer);
        duplicateOrder.addItem(new OrderItem(product, 5));
        SalesAnalyser analyser = new SalesAnalyser(
                List.of(product), List.of(firstOrder, duplicateOrder));

        assertEquals(List.of(2), analyser.findBestSellingProducts(3).stream()
                .map(ProductSales::unitsSold)
                .toList());
    }

    @Test
    void findBestSellingProducts_withNonPositiveLimit_throwsIllegalArgumentException() {
        SalesAnalyser analyser = createAnalyser();

        IllegalArgumentException zeroLimitException = assertThrows(
                IllegalArgumentException.class, () -> analyser.findBestSellingProducts(0));
        IllegalArgumentException negativeLimitException = assertThrows(
                IllegalArgumentException.class, () -> analyser.findBestSellingProducts(-1));

        assertEquals("Limit must be greater than zero", zeroLimitException.getMessage());
        assertEquals("Limit must be greater than zero", negativeLimitException.getMessage());
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

    @Test
    void findDuplicateOrderIds_returnsDuplicateIdsInEncounterOrder() {
        assertEquals(List.of("O007"), createAnalyser().findDuplicateOrderIds());
    }

    @Test
    void findDuplicateOrderIds_withNoOrders_returnsEmptyList() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(List.of(), analyser.findDuplicateOrderIds());
    }

    @Test
    void removeDuplicateOrders_keepsFirstOccurrenceInOrder() {
        List<Order> uniqueOrders = createAnalyser().removeDuplicateOrders();

        assertEquals(12, uniqueOrders.size());
        assertEquals(List.of("O001", "O002", "O003", "O004", "O005", "O006",
                        "O007", "O009", "O010", "O011", "O012", "O013"),
                uniqueOrders.stream().map(Order::getId).toList());
        assertEquals(1, uniqueOrders.stream().filter(order -> order.getId().equals("O007")).count());
        assertEquals(35_500.0, uniqueOrders.stream()
                .filter(order -> order.getId().equals("O007"))
                .findFirst()
                .orElseThrow()
                .calculateTotal());
    }

    @Test
    void removeDuplicateOrders_resultHasNoDuplicateOrderIds() {
        SalesAnalyser analyser = createAnalyser();
        SalesAnalyser deduplicatedAnalyser = new SalesAnalyser(
                loadProducts(), analyser.removeDuplicateOrders());

        assertEquals(List.of(), deduplicatedAnalyser.findDuplicateOrderIds());
    }

    @Test
    void findDuplicateOrderIds_withIdRepeatedThreeTimes_returnsIdOnce() {
        Customer customer = loadCustomers().get(0);
        List<Order> orders = List.of(
                new Order("REPEATED", customer),
                new Order("REPEATED", customer),
                new Order("REPEATED", customer));
        SalesAnalyser analyser = new SalesAnalyser(List.of(), orders);

        assertEquals(List.of("REPEATED"), analyser.findDuplicateOrderIds());
    }

    @Test
    void removeDuplicateOrders_withNoOrders_returnsEmptyList() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(List.of(), analyser.removeDuplicateOrders());
    }

    @Test
    void groupOrdersByStatus_groupsUniqueOrdersInEncounterOrder() {
        Map<OrderStatus, List<Order>> groups = createAnalyser().groupOrdersByStatus();

        assertEquals(List.of(OrderStatus.DELIVERED, OrderStatus.SHIPPED,
                        OrderStatus.PROCESSING, OrderStatus.PENDING, OrderStatus.CANCELLED),
                List.copyOf(groups.keySet()));
        assertEquals(List.of("O001", "O004", "O010"), groups.get(OrderStatus.DELIVERED)
                .stream().map(Order::getId).toList());
        assertEquals(List.of("O002", "O007"), groups.get(OrderStatus.SHIPPED)
                .stream().map(Order::getId).toList());
        assertEquals(1, groups.get(OrderStatus.SHIPPED).stream()
                .filter(order -> order.getId().equals("O007")).count());
        assertEquals(List.of("O003", "O012"), groups.get(OrderStatus.PROCESSING)
                .stream().map(Order::getId).toList());
        assertEquals(List.of("O005", "O009", "O013"), groups.get(OrderStatus.PENDING)
                .stream().map(Order::getId).toList());
        assertEquals(List.of("O006", "O011"), groups.get(OrderStatus.CANCELLED)
                .stream().map(Order::getId).toList());
    }

    @Test
    void groupOrdersByCustomerId_groupsUniqueOrdersAndOmitsCustomersWithNoOrders() {
        Map<String, List<Order>> groups = createAnalyser().groupOrdersByCustomerId();

        assertEquals(List.of("C001", "C002", "C003", "C004", "C005", "C006"),
                List.copyOf(groups.keySet()));
        assertEquals(List.of("O001", "O002", "O003", "O011"),
                groups.get("C001").stream().map(Order::getId).toList());
        assertEquals(List.of("O004", "O005"),
                groups.get("C002").stream().map(Order::getId).toList());
        assertEquals(List.of("O006"), groups.get("C003").stream().map(Order::getId).toList());
        assertEquals(List.of("O007", "O012"),
                groups.get("C004").stream().map(Order::getId).toList());
        assertEquals(List.of("O009"), groups.get("C005").stream().map(Order::getId).toList());
        assertEquals(List.of("O010", "O013"),
                groups.get("C006").stream().map(Order::getId).toList());
        assertFalse(groups.containsKey("C007"));
    }

    @Test
    void groupingMethods_withNoOrders_returnEmptyMaps() {
        SalesAnalyser analyser = new SalesAnalyser(List.of(), List.of());

        assertEquals(Map.of(), analyser.groupOrdersByStatus());
        assertEquals(Map.of(), analyser.groupOrdersByCustomerId());
    }
}
