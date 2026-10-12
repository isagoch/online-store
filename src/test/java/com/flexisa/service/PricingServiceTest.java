package com.flexisa.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.flexisa.discount.HighValueDiscountPolicy;
import com.flexisa.discount.PromoCodeDiscountPolicy;
import com.flexisa.discount.QuantityDiscountPolicy;
import com.flexisa.shipping.StandardShippingCalculator;
import java.util.List;

class PricingServiceTest {

    private final PricingService service = new PricingService(
            List.of(new QuantityDiscountPolicy(),
                    new HighValueDiscountPolicy(),
                    new PromoCodeDiscountPolicy()),
            new StandardShippingCalculator());

    @Test
    void calculateDiscountedTotal_WithQuantity30_Returns276000() {
        PricingContext context = new PricingContext(300000, 30, null, "Lagos");
        assertEquals(276000, service.calculateDiscountedTotal(context), 0.001);
    }
}