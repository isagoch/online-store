package com.flexisa.shipping;

import com.flexisa.service.PricingContext;

public interface ShippingCalculator {

    double calculateShippingFee(PricingContext context);

}