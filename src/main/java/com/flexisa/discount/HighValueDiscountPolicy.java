package com.flexisa.discount;

import com.flexisa.model.PricingContext;

public class HighValueDiscountPolicy implements DiscountPolicy {

    @Override
    public double calculateDiscount(PricingContext context) {
        double subtotal = context.subtotal();
        if (subtotal >= 200000) {
            return subtotal * 0.03;
        }
        return 0;
    }
}