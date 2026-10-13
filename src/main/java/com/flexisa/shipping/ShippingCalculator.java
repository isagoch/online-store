package com.flexisa.shipping;

import com.flexisa.model.PricingContext;

public interface ShippingCalculator {

    double calculateShippingFee(PricingContext context);

}