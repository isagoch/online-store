# Design Decisions

These are the choices I made while building the domain model for the online store, and why I made them.

## Why class, record or enum

- **Address and OrderItem are records.** Neither one changes after it is created, and neither needs its own identity. Two addresses with the same details are just the same address. A record fits because it is immutable and Java generates the accessors for me.
- **Product and Customer are classes.** They are real things with an identity. A product's stock goes up and down over time, so it needs to be a class.
- **Order, Payment and Delivery are classes.** Each one moves through different states, and I wanted methods with rules to control those moves.
- **OrderStatus, PaymentStatus and DeliveryStatus are enums.** The possible states are fixed and known, so an enum stops anyone from using a status that doesn't exist.

## How I handled validation

- If the input is bad (blank id, null object, negative amount), I throw `IllegalArgumentException`.
- If the input is fine but the object's current state doesn't allow the action (for example refunding a payment that isn't completed), I throw `IllegalStateException`.
- Constructors check everything first and only assign the fields at the end, so an invalid object can never be created.
- Fields are `private final` unless they have to change. Only the status fields change. I didn't add setters, so state can only change through methods that enforce the rules.
- Orders, payments and deliveries always start as PENDING. The status is not a constructor parameter, so nobody can create a payment that is already COMPLETED.

## Allowed status changes

- **Order:** PENDING can go to PROCESSING or CANCELLED. PROCESSING can go to SHIPPED or CANCELLED. SHIPPED can go to DELIVERED. DELIVERED and CANCELLED are final.
- **Payment:** PENDING can go to COMPLETED or FAILED. COMPLETED can go to REFUNDED.
- **Delivery:** PENDING goes to IN_TRANSIT, then DELIVERED, then RETURNED.

## Other decisions

- **I used double for money.** I kept it the same as Week 1 for consistency. In a real system I would use BigDecimal because double can give small rounding errors.
- **`getItems()` returns a copy.** If it returned the real list, someone could add items to it directly and get around the rule that items can only be added while the order is PENDING.
- **Several enums share status names.** PENDING and DELIVERED appear in more than one enum. That is fine because each enum is its own namespace, so `OrderStatus.PENDING` and `PaymentStatus.PENDING` don't clash.
- **PaymentStatus.PROCESSING is not used yet.** I kept Payment small for this task, but I left the value in the enum in case a future step needs it.
- **A payment's amount is not compared to the order total.** I only check that it is positive, to keep the model simple.
- **Order does not reduce product stock yet.** I left that for a later step.