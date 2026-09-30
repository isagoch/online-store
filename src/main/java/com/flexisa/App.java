package com.flexisa;

import javax.swing.JOptionPane;

public class App 
{
    //Method to calculae the discount 
    public static double calculateDiscount(double subtotal, int quantity) {
                if(quantity >= 25 && quantity < 50){
                return subtotal * 0.05;
                }
                else if(quantity >= 50 && quantity < 100){
                return subtotal * 0.10;
                }
                else if(quantity >= 100){
                return subtotal * 0.15;
                }
                else{
                    return 0.0;
                }

}
    //Method to calculate the delivery fee
    public static double calculateDeliveryFee(double subtotal, String location) {
                    if(subtotal >= 100000){
                    return 0.0;
                    }
                    else if(location.toUpperCase().equals("LAGOS")) {
                    return 1500;
                    }
                    else{
                    return 3000;
                    }
    }

    //Method to calculate the tax
    public static double calculateTax(double discounted_total){
                    return discounted_total * 0.02;
}

    //Method to calculate the subtotal
    public static double calculateSubtotal(double price, int quantity) {
                    return price * quantity;

    }
    //Method to calculate the total fee
    public static double calculateTotalFee(double discounted_total, double delivery_fee, double tax) {
                    return discounted_total + delivery_fee + tax;
    }
    
    //Method to calculate the discounted total
    public static double calculateDiscountedTotal(double subtotal, double discount) {
                    return subtotal - discount;
    }



    public static void main( String[] args )
    {
        // prompt the user to input the price, quantity and location
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
        String location = JOptionPane.showInputDialog("Enter location:");
        double tax = 0.0;
        double fee = 0.0;

        // validate input
        if(price > 0 && quantity > 0 && location != null && location.matches("[A-Za-z]+")) {
            //calculate subtotal
            double subtotal = calculateSubtotal(price, quantity);

            //calculate discount
            double discount = calculateDiscount(subtotal, quantity);

            //Implement the delivery Logic 
            double delivery_fee = calculateDeliveryFee(subtotal, location);

            //calculate and display the discounted price
            double discounted_total = calculateDiscountedTotal(subtotal, discount);

            // Implement the tax logic
            tax = calculateTax(discounted_total);

            // Calculate the total fee including discounted_total, delivery_fee and tax
            fee = calculateTotalFee(discounted_total, delivery_fee, tax);

            JOptionPane.showMessageDialog(null, "Discounted Total for " + location + ": $" + String.format("%.2f", discounted_total));
            JOptionPane.showMessageDialog(null, " Delivery fee: $" + String.format("%.2f", delivery_fee));
            JOptionPane.showMessageDialog(null, "Tax: $" + String.format("%.2f", tax));
            JOptionPane.showMessageDialog(null, "Total Fee: $" + String.format("%.2f", fee));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
        }
        
    }
}
