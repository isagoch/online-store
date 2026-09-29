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
        double discount = 0.0;
        String delivery_message = "";
        double delivery_fee = 0.0;

        // validate input
        if(price > 0 && quantity > 0 && location != null && location.matches("[A-Za-z]+")) {
            //calculate subtotal
            double subtotal = price * quantity;

            //calculate discount
            
            if(quantity >= 25 && quantity < 50){
                discount = subtotal * 0.05;
            }
            else if(quantity >= 50 && quantity < 100){
                discount = subtotal * 0.10;
            }
            else if(quantity >= 100){
                discount = subtotal * 0.15;
            }

            //Implement the delivery Logic 

            if(subtotal >= 100000){
                delivery_message = "Free delivery!";
            }
            else if(subtotal < 100000 && location.toUpperCase().equals("LAGOS")) {
                delivery_message = "Delivery charge applies.";
                delivery_fee = 1500;
            }
            else{
                delivery_message = "Delivery charge applies.";
                delivery_fee = 3000;
            }

            //calculate and display the discounted price
            double discounted_total = subtotal - discount;
            JOptionPane.showMessageDialog(null, "Discounted Total for " + location + ": $" + String.format("%.2f", discounted_total));
            JOptionPane.showMessageDialog(null, delivery_message + " Delivery fee: $" + String.format("%.2f", delivery_fee));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
        }
        
    }
}
