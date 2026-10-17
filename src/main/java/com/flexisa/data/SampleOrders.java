package com.flexisa.data;

import java.util.ArrayList;
import java.util.List;

import com.flexisa.model.Customer;
import com.flexisa.model.Order;
import com.flexisa.model.OrderItem;
import com.flexisa.model.OrderStatus;
import com.flexisa.model.Product;

public final class SampleOrders {

    private SampleOrders() {
    }
    public static List<Order> create(List<Customer> customers, List<Product> products) {
        List<Order> orders = new ArrayList<>();

        orders.add(buildOrder("O001", findCustomer(customers, "C001"), OrderStatus.DELIVERED,
                new OrderItem(findProduct(products, "P001"), 1),
                new OrderItem(findProduct(products, "P002"), 2)));
        orders.add(buildOrder("O002", findCustomer(customers, "C001"), OrderStatus.SHIPPED,
                new OrderItem(findProduct(products, "P003"), 1),
                new OrderItem(findProduct(products, "P004"), 1)));
        orders.add(buildOrder("O003", findCustomer(customers, "C001"), OrderStatus.PROCESSING,
                new OrderItem(findProduct(products, "P005"), 1),
                new OrderItem(findProduct(products, "P002"), 3)));
        orders.add(buildOrder("O004", findCustomer(customers, "C002"), OrderStatus.DELIVERED,
                new OrderItem(findProduct(products, "P009"), 1),
                new OrderItem(findProduct(products, "P008"), 2)));
        orders.add(buildOrder("O005", findCustomer(customers, "C002"), OrderStatus.PENDING,
                new OrderItem(findProduct(products, "P006"), 1)));
        orders.add(buildOrder("O006", findCustomer(customers, "C003"), OrderStatus.CANCELLED,
                new OrderItem(findProduct(products, "P001"), 2),
                new OrderItem(findProduct(products, "P005"), 2),
                new OrderItem(findProduct(products, "P009"), 3)));
        orders.add(buildOrder("O007", findCustomer(customers, "C004"), OrderStatus.SHIPPED,
                new OrderItem(findProduct(products, "P007"), 1),
                new OrderItem(findProduct(products, "P002"), 1)));
        // Accidental duplicate of the preceding order record.
        orders.add(buildOrder("O007", findCustomer(customers, "C004"), OrderStatus.SHIPPED,
                new OrderItem(findProduct(products, "P007"), 1),
                new OrderItem(findProduct(products, "P002"), 1)));
        orders.add(buildOrder("O009", findCustomer(customers, "C005"), OrderStatus.PENDING));
        orders.add(buildOrder("O010", findCustomer(customers, "C006"), OrderStatus.DELIVERED,
                new OrderItem(findProduct(products, "P008"), 1),
                new OrderItem(findProduct(products, "P003"), 2),
                new OrderItem(findProduct(products, "P006"), 2)));
        orders.add(buildOrder("O011", findCustomer(customers, "C001"), OrderStatus.CANCELLED,
                new OrderItem(findProduct(products, "P003"), 3),
                new OrderItem(findProduct(products, "P004"), 2)));
        orders.add(buildOrder("O012", findCustomer(customers, "C004"), OrderStatus.PROCESSING,
                new OrderItem(findProduct(products, "P005"), 1),
                new OrderItem(findProduct(products, "P002"), 4)));
        orders.add(buildOrder("O013", findCustomer(customers, "C006"), OrderStatus.PENDING,
                new OrderItem(findProduct(products, "P007"), 2),
                new OrderItem(findProduct(products, "P006"), 1)));

        return orders;
    }

    private static Order buildOrder(String id, Customer customer, OrderStatus targetStatus,
                                    OrderItem... items) {
        Order order = new Order(id, customer);
        for (OrderItem item : items) {
            order.addItem(item);
        }

        switch (targetStatus) {
            case PENDING -> {
            }
            case PROCESSING -> order.startProcessing();
            case SHIPPED -> {
                order.startProcessing();
                order.ship();
            }
            case DELIVERED -> {
                order.startProcessing();
                order.ship();
                order.deliver();
            }
            case CANCELLED -> order.cancel();
        }

        return order;
    }

    private static Customer findCustomer(List<Customer> customers, String id) {
        for (Customer customer : customers) {
            // Loaded resources can contain duplicate IDs; the first match is intentional.
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        throw new IllegalStateException("Customer not found: " + id);
    }

    private static Product findProduct(List<Product> products, String id) {
        for (Product product : products) {
            // Loaded resources can contain duplicate IDs; the first match is intentional.
            if (product.getId().equals(id)) {
                return product;
            }
        }
        throw new IllegalStateException("Product not found: " + id);
    }
}
