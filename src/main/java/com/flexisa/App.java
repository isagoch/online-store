package com.flexisa;

import java.util.List;
import java.util.Scanner;

import com.flexisa.discount.HighValueDiscountPolicy;
import com.flexisa.discount.PromoCodeDiscountPolicy;
import com.flexisa.discount.QuantityDiscountPolicy;
import com.flexisa.service.PricingContext;
import com.flexisa.service.PricingService;
import com.flexisa.shipping.StandardShippingCalculator;
import com.flexisa.validation.OrderInputValidator;

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

    private static PricingService createPricingService() {
        return new PricingService(
            List.of(new QuantityDiscountPolicy(),
                    new HighValueDiscountPolicy(),
                    new PromoCodeDiscountPolicy()),
            new StandardShippingCalculator());
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            double price = readDouble(scanner, "Enter the price of the item: ");
            int quantity = readInt(scanner, "Enter the quantity of the item: ");
            String location = readString(scanner, "Enter the location: ");
            String promoCode = readString(scanner, "Enter the promo code (if any): ");

            OrderInputValidator.validateOrder(price, quantity, location);
            
            double subtotal = price * quantity;
            PricingContext context = new PricingContext(subtotal, quantity, promoCode, location);
            PricingService pricingService = createPricingService();

            double discountedTotal = pricingService.calculateDiscountedTotal(context);
            double deliveryFee = pricingService.calculateDeliveryFee(context);
            double tax = pricingService.calculateTax(context);
            double fee = pricingService.calculateTotalFee(context);

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
