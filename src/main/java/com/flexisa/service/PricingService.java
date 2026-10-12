package com.flexisa.service;

import com.flexisa.discount.DiscountPolicy;
import com.flexisa.shipping.ShippingCalculator;
import java.util.List;

public class PricingService {

    private static final double TAX_RATE = 0.02;
    
    private final List<DiscountPolicy> discountPolicies;
    private final ShippingCalculator shippingCalculator;

    public PricingService(List<DiscountPolicy> discountPolicies, ShippingCalculator shippingCalculator) {
        this.discountPolicies = List.copyOf(discountPolicies);
        this.shippingCalculator = shippingCalculator;
    }

    public double calculateTotalDiscount(PricingContext context) {
        double total = 0;
        for (DiscountPolicy policy : discountPolicies) {
            total += policy.calculateDiscount(context);
        }
        return total;
    }

    public double calculateDiscountedTotal(PricingContext context) {
        double subtotal = context.subtotal();
        double totalDiscount = calculateTotalDiscount(context);
        return subtotal - totalDiscount;
    }

    public double calculateDeliveryFee(PricingContext context) {
        return shippingCalculator.calculateShippingFee(context);
    }

    public double calculateTax(PricingContext context) {
        double discountedTotal = calculateDiscountedTotal(context);
        return discountedTotal * TAX_RATE;
    }

}