package com.flexisa.discount;

import com.flexisa.model.PricingContext;

public interface DiscountPolicy {

    double calculateDiscount(PricingContext context);
}