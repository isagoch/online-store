package com.flexisa.discount;

import com.flexisa.model.PricingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HighValueDiscountPolicyTest {

    private final HighValueDiscountPolicy policy = new HighValueDiscountPolicy();

    @Test
    void calculateDiscount_WithSubtotal199999_ReturnsZero() {
        PricingContext context = new PricingContext(199999, 1, null, null);
        assertEquals(0, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithSubtotal200000_Returns6000() {
        PricingContext context = new PricingContext(200000, 1, null, null);
        assertEquals(6000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithSubtotal300000_Returns9000() {
        PricingContext context = new PricingContext(300000, 1, null, null);
        assertEquals(9000, policy.calculateDiscount(context), 0.001);
    }
}
