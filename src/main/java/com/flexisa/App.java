package com.flexisa;

import javax.swing.JOptionPane;

public class App 
{
    public static void main( String[] args )
    {
        // first lets take in price, quantity and location
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity:"));
        String location = JOptionPane.showInputDialog("Enter location:");
        double discount = 0.0;

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
            double discounted_total = subtotal - discount;
            JOptionPane.showMessageDialog(null, "Discounted Total for " + location + ": $" + String.format("%.2f", discounted_total));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
        }
        
    }
}
