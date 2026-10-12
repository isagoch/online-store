package com.flexisa.service;

import com.flexisa.discount.DiscountPolicy;
import com.flexisa.shipping.ShippingCalculator;
import java.util.List;

public class PricingService {

    private final List<DiscountPolicy> discountPolicies;
    private final ShippingCalculator shippingCalculator;

    public PricingService(List<DiscountPolicy> discountPolicies, ShippingCalculator shippingCalculator) {
        this.discountPolicies = List.copyOf(discountPolicies);
        this.shippingCalculator = shippingCalculator;
    }
}