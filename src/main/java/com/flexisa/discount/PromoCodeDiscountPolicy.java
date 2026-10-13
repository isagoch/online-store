package com.flexisa.discount;

import com.flexisa.model.PricingContext;

public class PromoCodeDiscountPolicy implements DiscountPolicy {

    @Override
    public double calculateDiscount(PricingContext context) {
        String promoCode = context.promoCode();
        double subtotal = context.subtotal();
        if (promoCode == null) {
            return 0;
        }
        String code = promoCode.trim();
        if (code.equalsIgnoreCase("FLEXISAF")) {
            return subtotal * 0.05;
        }
        if (code.equalsIgnoreCase("FLEXISA10")) {
            return subtotal * 0.10;
        }
        if (code.equalsIgnoreCase("FLEXISA15")) {
            return subtotal * 0.15;
        }
        return 0;
    }
}
