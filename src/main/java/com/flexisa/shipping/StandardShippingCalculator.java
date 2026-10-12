package com.flexisa.shipping;

import com.flexisa.service.PricingContext;

public class StandardShippingCalculator implements ShippingCalculator {

    @Override
    public double calculateShippingFee(PricingContext context) {
        double subtotal = context.subtotal();
        String location = context.location();
        if (subtotal >= 100000) {
            return 0;
        }
        if (location != null && location.trim().equalsIgnoreCase("LAGOS")) {
            return 1500;
        }
        return 3000;
    }
}
