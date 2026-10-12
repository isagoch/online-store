package com.flexisa;

import java.util.Scanner;

public class App {
    private static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine());
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    private static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            double price = readDouble(scanner, "Enter the price of the item: ");
            int quantity = readInt(scanner, "Enter the quantity of the item: ");
            String location = readString(scanner, "Enter the location: ");
            String promoCode = readString(scanner, "Enter the promo code (if any): ");

            OrderPricing.validateOrder(price, quantity, location);

            double subtotal = OrderPricing.calculateSubtotal(price, quantity);
            double quantityDiscount = OrderPricing.calculateDiscount(subtotal, quantity);
            double highValueDiscount = OrderPricing.calculateHighValueDiscount(subtotal);
            double promoDiscount = OrderPricing.calculatePromoDiscount(subtotal, promoCode);
            double totalDiscount = quantityDiscount + highValueDiscount + promoDiscount;

            double deliveryFee = OrderPricing.calculateDeliveryFee(subtotal, location);
            double discountedTotal = OrderPricing.calculateDiscountedTotal(subtotal, totalDiscount);
            double tax = OrderPricing.calculateTax(discountedTotal);
            double fee = OrderPricing.calculateTotalFee(discountedTotal, deliveryFee, tax);

            System.out.println("Discounted Total for " + location + ": #" + String.format("%.2f", discountedTotal));
            System.out.println("Delivery fee: #" + String.format("%.2f", deliveryFee));
            System.out.println("Tax: #" + String.format("%.2f", tax));
            System.out.println("Total Fee: #" + String.format("%.2f", fee));
        } catch (NumberFormatException e) {
            System.err.println("Input Error: Please enter valid numeric values for price and quantity.");
        } catch (IllegalArgumentException e) {
            System.err.println("Input Error: " + e.getMessage());
        }
    }
}
