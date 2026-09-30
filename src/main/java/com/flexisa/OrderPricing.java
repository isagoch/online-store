package com.flexisa;

public class OrderPricing {
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



}
