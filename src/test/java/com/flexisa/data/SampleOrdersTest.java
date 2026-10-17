package com.flexisa.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.flexisa.model.Customer;
import com.flexisa.model.Order;
import com.flexisa.model.OrderStatus;
import com.flexisa.model.Product;

class SampleOrdersTest {

    private List<Order> createSampleOrders() {
        List<Customer> customers = DataLoader.parseAll(
                DataLoader.readResourceLines("customers.csv"),
                CustomerParser::parseLine).valid();
        List<Product> products = DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine).valid();

        return SampleOrders.create(customers, products);
    }

    @Test
    void create_buildsExpectedNumberOfOrders() {
        assertEquals(13, createSampleOrders().size());
    }

    @Test
    void create_includesEveryOrderStatus() {
        Set<OrderStatus> statuses = createSampleOrders().stream()
                .map(Order::getStatus)
                .collect(Collectors.toSet());

        assertTrue(statuses.containsAll(Set.of(OrderStatus.values())));
    }

    @Test
    void create_containsTheAccidentallyDuplicatedOrderIdTwice() {
        List<Order> orders = createSampleOrders();
        long duplicateCount = orders.stream()
                .filter(order -> order.getId().equals("O007"))
                .count();

        assertEquals(2, duplicateCount);
    }

    @Test
    void create_orderO001_hasHandCalculatedTotal() {
        Order order = createSampleOrders().stream()
                .filter(candidate -> candidate.getId().equals("O001"))
                .findFirst()
                .orElseThrow();

        assertEquals(467000.0, order.calculateTotal());
    }
}