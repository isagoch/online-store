# Online Store Order Pricing and Delivery Calculator

## What it does
It calculates an order's subtotal, discount, delivery fee, tax, and final amount.

## Setup
- Java 21, Maven, and VS Code.
- Open the project in VS Code, open `App.java`, and click **Run** above the `main` method.

## Business rules
- **Quantity discount:** Buy 25–49 items for 5% off, 50–99 items for 10% off, and 100+ items for 15% off. Under 25 items gets 0%.
- **High-value discount:** A 3% discount applies when the subtotal is 200000 or more.
- **Promo codes:** FLEXISAF, FLEXISA10, and FLEXISA15 give 5%, 10%, and 15% off the subtotal. All other codes give 0%.
- **Delivery fee:**
  - If the subtotal is #100,000 or more, delivery is free, regardless of location.
  - Otherwise, Lagos is #1,500.
  - Outside Lagos, it is #3,000.
- **Tax:** 2% of the amount after discounts and before delivery.
- **How the discounts combine:** Each discount is calculated from the original subtotal. Then they are added together.

## Usage
The app asks for price, quantity, location, and promo code. See `SAMPLE_RUNS.md` for examples.

## Running the tests
Run `mvn test`. The screenshot is at `docs/test-results.png`.

## Domain model (Week 2)
The `com.flexisa.model` package models an e-commerce order system. The code is in `src/main/java/com/flexisa/model/` and the tests are in `src/test/java/com/flexisa/model/`.

### Types
- **Records (immutable):** `Address`, `OrderItem`
- **Classes (entities with changing state):** `Customer`, `Product`, `Order`, `Payment`, `Delivery`
- **Enums (fixed states):** `OrderStatus`, `PaymentStatus`, `DeliveryStatus`

### Relationships
- A `Customer` has one `Address`.
- An `Order` is placed by one `Customer` and contains many `OrderItem`s.
- An `OrderItem` refers to one `Product` and has a quantity.
- A `Payment` is for one `Order`.
- A `Delivery` is for one `Order` and ships to one `Address`.

### Rules
- Invalid input throws `IllegalArgumentException`.
- An action the current state does not allow throws `IllegalStateException`.
- Orders, payments and deliveries start as PENDING and can only change status through methods with rules (for example, an order cannot be shipped before it is processing).

### More details
- Class diagram: `docs/class-diagram.md`
- Design decisions: `docs/design-decisions.md`
- Run the model tests with `mvn test`.