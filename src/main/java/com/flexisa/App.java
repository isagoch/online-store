package com.flexisa;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        // prompt the user to input the price, quantity and location
        try (Scanner scanner = new Scanner(System.in)) {
        System.out.print("Enter the price of the item: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter the quantity of the item: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the location: ");
        String location = scanner.nextLine();

        System.out.print("Enter the promo code (if any): ");
        String promoCode = scanner.nextLine();

        // validate input
        OrderPricing.validateOrder(price, quantity, location);

        // calculate subtotal
        double subtotal = OrderPricing.calculateSubtotal(price, quantity);

        // calculate discount
        // calculate discounts
        double quantityDiscount = OrderPricing.calculateDiscount(subtotal, quantity);
        double highValueDiscount = OrderPricing.calculateHighValueDiscount(subtotal);
        double promoDiscount = OrderPricing.calculatePromoDiscount(subtotal, promoCode);
        double totalDiscount = quantityDiscount + highValueDiscount + promoDiscount;

            //Implement the delivery Logic
        double delivery_fee = OrderPricing.calculateDeliveryFee(subtotal, location);

            //calculate and display the discounted price
        double discounted_total = OrderPricing.calculateDiscountedTotal(subtotal, totalDiscount);

            // Implement the tax logic
        double tax = OrderPricing.calculateTax(discounted_total);

            // Calculate the total fee including discounted_total, delivery_fee and tax
        double fee = OrderPricing.calculateTotalFee(discounted_total, delivery_fee, tax);

        System.out.println("Discounted Total for " + location + ": #" + String.format("%.2f", discounted_total));
        System.out.println("Delivery fee: #" + String.format("%.2f", delivery_fee));
        System.out.println("Tax: #" + String.format("%.2f", tax));
        System.out.println("Total Fee: #" + String.format("%.2f", fee));
    }

    catch (NumberFormatException e) {
            System.err.println("Input Error: Please enter valid numeric values for price and quantity.");
    }
    catch (IllegalArgumentException e) {
            System.err.println("Input Error: " + e.getMessage());
    }

    }
}
