package com.flexisa.discount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.flexisa.service.PricingContext;

class QuantityDiscountPolicyTest {

    private final QuantityDiscountPolicy policy = new QuantityDiscountPolicy();

    @Test
    void calculateDiscount_WithQuantity24_ReturnsZero() {
        PricingContext context = new PricingContext(100000, 24, null, null);
        assertEquals(0, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithQuantity25_Returns5000() {
        PricingContext context = new PricingContext(100000, 25, null, null);
        assertEquals(5000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithQuantity49_Returns5000() {
        PricingContext context = new PricingContext(100000, 49, null, null);
        assertEquals(5000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithQuantity50_Returns10000() {
        PricingContext context = new PricingContext(100000, 50, null, null);
        assertEquals(10000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithQuantity99_Returns10000() {
        PricingContext context = new PricingContext(100000, 99, null, null);
        assertEquals(10000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithQuantity100_Returns15000() {
        PricingContext context = new PricingContext(100000, 100, null, null);
        assertEquals(15000, policy.calculateDiscount(context), 0.001);
    }
}