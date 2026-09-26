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

        // validate input
        if(price > 0 && quantity > 0 && location != null && location.matches("[A-Za-z]+")) {
            //calculate subtotal
            double subtotal = price * quantity;

        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
        }
        
    }
}
