package com.flexisa.discount;

import com.flexisa.model.PricingContext;

public class QuantityDiscountPolicy implements DiscountPolicy {

    @Override
    public double calculateDiscount(PricingContext context) {
        int quantity = context.quantity();
        double subtotal = context.subtotal();

        if (quantity >= 100) {
            return subtotal * 0.15;
        }

        if (quantity >= 50) {
            return subtotal * 0.10;
        }
        if (quantity >= 25) {
            return subtotal * 0.05;
        }
        return 0;
    }
}
