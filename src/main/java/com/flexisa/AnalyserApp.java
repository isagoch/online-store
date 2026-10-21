package com.flexisa;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.flexisa.data.CustomerParser;
import com.flexisa.data.DataLoader;
import com.flexisa.data.LoadResult;
import com.flexisa.data.ProductParser;
import com.flexisa.data.SampleOrders;
import com.flexisa.model.Customer;
import com.flexisa.model.Order;
import com.flexisa.model.OrderStatus;
import com.flexisa.model.Product;
import com.flexisa.service.SalesAnalyser;

public final class AnalyserApp {

    private AnalyserApp() {
    }

    public static void main(String[] args) {
        LoadResult<Product> productResult = DataLoader.parseAll(
                DataLoader.readResourceLines("products.csv"),
                ProductParser::parseLine);
        List<Product> products = productResult.valid();
        printLoadResult("Products", productResult);

        LoadResult<Customer> customerResult = DataLoader.parseAll(
                DataLoader.readResourceLines("customers.csv"),
                CustomerParser::parseLine);
        List<Customer> customers = customerResult.valid();
        printLoadResult("Customers", customerResult);

        SalesAnalyser analyser = new SalesAnalyser(
                products, SampleOrders.create(customers, products));
        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, analyser);
        }
    }

    private static void runMenu(Scanner scanner, SalesAnalyser analyser) {
        boolean running = true;
        while (running && scanner.hasNextLine()) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Enter product keyword: ");
                        printProducts(analyser.searchProducts(scanner.nextLine()));
                    }
                    case "2" -> {
                        System.out.print("Enter order keyword: ");
                        printOrders(analyser.searchOrders(scanner.nextLine()));
                    }
                    case "3" -> filterByStatus(scanner, analyser);
                    case "4" -> filterByCustomer(scanner, analyser);
                    case "5" -> filterByTotalRange(scanner, analyser);
                    case "9" -> System.out.println(analyser.buildSalesSummary(3));
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option: " + choice);
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Input Error: " + exception.getMessage());
            }
        }
    }

    private static void filterByStatus(Scanner scanner, SalesAnalyser analyser) {
        System.out.print("Enter order status: ");
        String text = scanner.nextLine().trim();
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown status: " + text);
        }
        printOrders(analyser.filterOrdersByStatus(status));
    }

    private static void filterByCustomer(Scanner scanner, SalesAnalyser analyser) {
        System.out.print("Enter customer ID: ");
        String customerId = scanner.nextLine().trim();
        printOrders(analyser.filterOrdersByCustomerId(customerId));
    }

    private static void filterByTotalRange(Scanner scanner, SalesAnalyser analyser) {
        System.out.print("Enter minimum total: ");
        double min = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter maximum total: ");
        double max = Double.parseDouble(scanner.nextLine().trim());
        printOrders(analyser.filterOrdersByTotalRange(min, max));
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Sales Analyser");
        System.out.println("1. Search products");
        System.out.println("2. Search orders");
        System.out.println("3. Filter orders by status");
        System.out.println("4. Filter orders by customer");
        System.out.println("5. Filter orders by total range");
        System.out.println("9. Sales summary");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void printLoadResult(String label, LoadResult<?> result) {
        System.out.println(label + " loaded: " + result.valid().size());
        result.errors().forEach(System.out::println);
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No results");
            return;
        }
        products.forEach(product -> System.out.printf(
                Locale.ROOT, "%s %s (#%.2f)%n", product.getId(), product.getName(), product.getPrice()));
    }

    private static void printOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No results");
            return;
        }
        orders.forEach(order -> System.out.printf(
                Locale.ROOT,
                "%s %s %s (#%.2f)%n",
                order.getId(),
                order.getCustomer().getName(),
                order.getStatus(),
                order.calculateTotal()));
    }
}