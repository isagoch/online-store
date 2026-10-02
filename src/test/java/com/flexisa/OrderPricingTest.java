package com.flexisa;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderPricingTest {
    @Test
    void calculateDiscountedTotal_WithQuantity30_Returns276000() {
        double subtotal = OrderPricing.calculateSubtotal(10000, 30);
        double quantityDiscount = OrderPricing.calculateDiscount(subtotal, 30);
        double highValueDiscount = OrderPricing.calculateHighValueDiscount(subtotal);
        String promoCode = "";
        double promoDiscount = OrderPricing.calculatePromoDiscount(subtotal, promoCode);
        double totalDiscount = quantityDiscount + highValueDiscount + promoDiscount;
        double discountedTotal = OrderPricing.calculateDiscountedTotal(subtotal, totalDiscount);
        assertEquals(276000, discountedTotal, 0.01);
        
    }
}
