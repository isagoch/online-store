package com.flexisa.discount;

import com.flexisa.model.PricingContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromoCodeDiscountPolicyTest {

    private final PromoCodeDiscountPolicy policy = new PromoCodeDiscountPolicy();

    @Test
    void calculateDiscount_WithPromoCodeFLEXISAF_Returns5000() {
        PricingContext context = new PricingContext(100000, 1, "FLEXISAF", null);
        assertEquals(5000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithPromoCodeFLEXISA10_Returns10000() {
        PricingContext context = new PricingContext(100000, 1, "FLEXISA10", null);
        assertEquals(10000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithPromoCodeFLEXISA15_Returns15000() {
        PricingContext context = new PricingContext(100000, 1, "FLEXISA15", null);
        assertEquals(15000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithLowercasePromoCode_Returns10000() {
        PricingContext context = new PricingContext(100000, 1, "flexisa10", null);
        assertEquals(10000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithWhitespaceAroundPromoCode_Returns15000() {
        PricingContext context = new PricingContext(100000, 1, " FLEXISA15 ", null);
        assertEquals(15000, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithUnknownPromoCode_ReturnsZero() {
        PricingContext context = new PricingContext(100000, 1, "INVALID", null);
        assertEquals(0, policy.calculateDiscount(context), 0.001);
    }

    @Test
    void calculateDiscount_WithNullPromoCode_ReturnsZero() {
        PricingContext context = new PricingContext(100000, 1, null, null);
        assertEquals(0, policy.calculateDiscount(context), 0.001);
    }
}