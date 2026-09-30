package com.flexisa;

import javax.swing.JOptionPane;

public class App 
{

    public static void main( String[] args )
    {
        // prompt the user to input the price, quantity and location
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
        String location = JOptionPane.showInputDialog("Enter location:");

    try {
        // validate input
        OrderPricing.validateOrder(price, quantity, location);

        // calculate subtotal
        double subtotal = OrderPricing.calculateSubtotal(price, quantity);

        // calculate discount
        double discount = OrderPricing.calculateDiscount(subtotal, quantity);

            //Implement the delivery Logic
        double delivery_fee = OrderPricing.calculateDeliveryFee(subtotal, location);

            //calculate and display the discounted price
        double discounted_total = OrderPricing.calculateDiscountedTotal(subtotal, discount);

            // Implement the tax logic
        double tax = OrderPricing.calculateTax(discounted_total);

            // Calculate the total fee including discounted_total, delivery_fee and tax
        double fee = OrderPricing.calculateTotalFee(discounted_total, delivery_fee, tax);

        JOptionPane.showMessageDialog(null, "Discounted Total for " + location + ": #" + String.format("%.2f", discounted_total));
        JOptionPane.showMessageDialog(null, " Delivery fee: #" + String.format("%.2f", delivery_fee));
        String taxMessage = "Tax: #" + String.format("%.2f", tax);
        JOptionPane.showMessageDialog(null, taxMessage);
        JOptionPane.showMessageDialog(null, "Total Fee: #" + String.format("%.2f", fee));
    }
    catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    }
}
