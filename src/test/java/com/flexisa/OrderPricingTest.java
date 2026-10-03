package com.flexisa;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void calculateDiscountedTotal_WithPromoCode_Returns246000() {
        double subtotal = OrderPricing.calculateSubtotal(10000, 30);
        double quantityDiscount = OrderPricing.calculateDiscount(subtotal, 30);
        double highValueDiscount = OrderPricing.calculateHighValueDiscount(subtotal);
        String promoCode = "FLEXISA10";
        double promoDiscount = OrderPricing.calculatePromoDiscount(subtotal, promoCode);
        double totalDiscount = quantityDiscount + highValueDiscount + promoDiscount;
        double discountedTotal = OrderPricing.calculateDiscountedTotal(subtotal, totalDiscount);
        assertEquals(246000, discountedTotal, 0.01);
    }

    @Test
    void validateOrder_WithNegativeQuantity_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderPricing.validateOrder(10000, -5, "Lagos");
        });
        assertEquals("Please input quantity greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithZeroPrice_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderPricing.validateOrder(0, 5, "Lagos");
        });
        assertEquals("Please input a price greater than zero", exception.getMessage());
    }

    @Test
    void validateOrder_WithEmptyLocation_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderPricing.validateOrder(10000, 5, "");
        });
        assertEquals("Please input a valid location", exception.getMessage());
    }
}
