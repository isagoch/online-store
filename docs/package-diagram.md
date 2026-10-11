# Package Diagram

## Before (Week 2 structure)

```mermaid
flowchart TB
    subgraph root["com.flexisa  (Week 1 code)"]
        App
        OrderPricing
    end

    subgraph model["com.flexisa.model  (Week 2 code)"]
        Customer
        Address
        Order
        OrderItem
        Product
        Payment
        Delivery
        OrderStatus
        PaymentStatus
        DeliveryStatus
    end

    App --> OrderPricing
    Order --> Customer
    Order --> OrderItem
    Order --> OrderStatus
    Customer --> Address
    OrderItem --> Product
    Payment --> Order
    Payment --> PaymentStatus
    Delivery --> Order
    Delivery --> Address
    Delivery --> DeliveryStatus
```

### Observations (problems this structure has)

- Everything outside `model` sits in one root package with no layering.
- The Week 1 pricing code (`OrderPricing`) and the Week 2 model are not connected.
  `Order.calculateTotal()` ignores discounts, delivery fee and tax.
- `OrderPricing` mixes input validation with every pricing rule (low cohesion).
- `App.main` reads input, validates, calculates and prints in one method.
- Discount thresholds, rates and promo codes are hard-coded inside methods,
  so adding a new discount means editing existing code.
- The same blank/null/positive checks are repeated across model classes.