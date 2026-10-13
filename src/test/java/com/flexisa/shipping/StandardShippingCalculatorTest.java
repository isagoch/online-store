package com.flexisa.shipping;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.flexisa.model.PricingContext;

class StandardShippingCalculatorTest {

    private final StandardShippingCalculator calculator = new StandardShippingCalculator();

    @Test
    void calculateShippingFee_WithSubtotal100000_ReturnsZero() {
        PricingContext context = new PricingContext(100000, 1, null, "Lagos");
        assertEquals(0, calculator.calculateShippingFee(context), 0.001);
    }

    @Test
    void calculateShippingFee_WithSubtotal99999InLagos_Returns1500() {
        PricingContext context = new PricingContext(99999, 1, null, "Lagos");
        assertEquals(1500, calculator.calculateShippingFee(context), 0.001);
    }

    @Test
    void calculateShippingFee_WithMessyLagosInput_Returns1500() {
        PricingContext context = new PricingContext(50000, 1, null, " lagos ");
        assertEquals(1500, calculator.calculateShippingFee(context), 0.001);
    }

    @Test
    void calculateShippingFee_WithAbuja_Returns3000() {
        PricingContext context = new PricingContext(50000, 1, null, "Abuja");
        assertEquals(3000, calculator.calculateShippingFee(context), 0.001);
    }

    @Test
    void calculateShippingFee_WithNullLocation_Returns3000() {
        PricingContext context = new PricingContext(50000, 1, null, null);
        assertEquals(3000, calculator.calculateShippingFee(context), 0.001);
    }
}