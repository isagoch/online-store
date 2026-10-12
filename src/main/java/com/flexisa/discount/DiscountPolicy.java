package com.flexisa.discount;

import com.flexisa.service.PricingContext;

public interface DiscountPolicy {

    double calculateDiscount(PricingContext context);
}