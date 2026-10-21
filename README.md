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

## Customer data
`src/main/resources/customers.csv` stores one customer per line using the
`id,name,email,street,city,state,country,postalCode` columns. This simple
comma-separated format does not support quoted fields, so values (including
street addresses) must not contain commas.

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

## Refactoring decisions (Week 3)

### Package structure
`model`, `service`, `discount`, `shipping`, `validation`. See
`docs/package-diagram.md` for the before and after diagrams.

### What I did and why
- **PricingContext (in `model`)**: the three discount rules need different inputs.
  One data record gives every interface method the same signature. It first lived
  in `service`, which created a cycle with `discount` and `shipping`, so I moved
  it into `model`.
- **Two interfaces, four implementations**: `DiscountPolicy` (quantity, high-value
  and promo code) and `ShippingCalculator` (standard). Each rule was hard-coded
  before, and now rules can be added or replaced without editing the service.
  I did not add `PaymentProcessor` because nothing in the code processes payments
  and I would have been inventing behaviour.
- **Composition over inheritance**: `PricingService` holds a list of
  `DiscountPolicy` objects and a `ShippingCalculator`, passed in through the
  constructor. It never mentions concrete classes. I used `List.copyOf` so the
  caller cannot change the list afterwards.
- **Validation extracted**: `OrderInputValidator` replaces the validation that
  was mixed into `OrderPricing`. The messages are identical.
- **App**: `main` was split into small methods for reading, creating the service
  and printing. The output format is unchanged.
- **OrderPricing removed**: once nothing used it, I deleted it.
- **No `repository` or `exception` package**: the project has no persistence,
  and custom exceptions would change behaviour that the tests lock in.

### Behaviour preserved: old tests to new tests
| Old test | Now covered by |
|---|---|
| Quantity 30 gives 276000 | `PricingServiceTest` |
| Promo code gives 246000 | `PricingServiceTest` |
| Negative quantity rejected | `OrderInputValidatorTest` |
| Zero price rejected | `OrderInputValidatorTest` |
| Empty location rejected | `OrderInputValidatorTest` |
| Quantity 24 gives 0, 25 gives 5000 | `QuantityDiscountPolicyTest` |
| High-value 6000 at 200000 | `HighValueDiscountPolicyTest` |
| Lagos delivery 1500 | `StandardShippingCalculatorTest` |

### Testing
69 tests. New tests cover each discount policy, the shipping calculator,
`PricingService` and the validator, including boundary values.

### Known limitations
- `calculateDiscountedTotal` runs twice inside `calculateTotalFee`. This is
  harmless, and I chose simple code over caching.
- A NaN price passes validation, as it did in Week 1. I left it to preserve
  behaviour.
- The Week 2 `Order` still ignores discounts, delivery and tax, so it is not
  connected to `PricingService`.
- The model cleanup (repeated checks and style slips in `Customer`, `Order` and
  `Product`) is future work.

## Sales analysis
- Run `com.flexisa.AnalyserApp` to load the sample data, report rejected records,
  and print the sales summary. The original `App` remains the Week 1–3 price
  calculator.
- Grouping and sales calculations use deduplicated orders, keeping the first
  occurrence of each order ID. Cancelled orders are excluded from total sales;
  sales figures are plain item totals, with no discounts, delivery, or tax.
  Average order value uses the same qualifying orders, counts orders with no
  items as value 0, and returns 0.0 when there are no qualifying orders.
  Best-selling products use the same qualifying orders and rank by units sold,
  with ties broken by product ID in ascending order. The requested limit must
  be positive. A limit of 3 includes P003 but excludes P007 and P008, which tie
  at 3 units; the cutoff follows the ID tie-break, not a difference in sales.
  The sales summary reports qualifying orders and excluded duplicate and
  cancelled records; with no orders, it shows zero values and `No products
  sold`. Searches and filters continue to show raw records.
- Order groups are created only for statuses or customers that have orders.
  An absent group can be read with `groups.getOrDefault(key, List.of())`.
- Grouping returns a map because callers look up all orders for a status or
  customer ID directly by key; map keys retain the order in which they first
  appear in the dataset.

  ## Collections, generics and data processing (Week 4)

### Setup and usage
- Java 21 and Maven. Run `mvn clean test` to run all the tests.
- To run the analyser: `mvn clean compile`, then `java -cp target/classes com.flexisa.AnalyserApp`.
- The menu has these options: 1 search products, 2 search orders, 3 filter orders by status, 4 filter orders by customer, 5 filter orders by total range, 9 sales summary, 0 exit.
- Sorting, duplicate removal and grouping are done and tested in `SalesAnalyser`, but they are not in the menu yet.
- Bad input prints `Input Error: ...` and goes back to the menu.
- `SAMPLE_RUNS.md` has sample runs.

### Sample data
- `products.csv` and `customers.csv` are in `src/main/resources`. They have duplicates and invalid lines on purpose, so i can test those cases.
- Orders are built in Java (`SampleOrders`). An order always starts PENDING, so it has to be moved through its steps and can't just be written in a file.
- The loader reports each rejected line with its line number. The numbers count data lines after the header.
- "Loaded" counts include duplicates, because duplicates are removed later in the analyser.

### Collections
- I used a `List`, a `Set` and a `Map`. See `docs/collection-selection.md` for why.
- Best sellers use a `Map` from product to units sold.

### Generic method
`DataLoader.parseAll` and `LoadResult<T>` work for any type. I use them for both products and customers.

### Design decisions
- Date filtering is skipped because `Order` has no date field.
- Two records are the same if they have the same id. The first one is kept.
- Search and filters show raw records, so duplicates still show. Grouping and sales figures use unique orders only.
- Order value means the plain item total from `calculateTotal()`. Filters don't remove cancelled orders.
- Total range bounds are inclusive. A negative bound is accepted and works like 0, because totals are never negative.
- A null or blank search keyword is rejected instead of returning everything.
- Sorting is ascending only, with one method each for price, name and stock.
- `Locale.ROOT` is used for matching and money formatting, so results are the same on every machine.
- The CSV format has no quoted commas, so values can't contain commas.
- I made a separate `AnalyserApp` so the old `App` stays the same.

### Testing
There are 173 tests. They cover empty collections, duplicates, invalid records, missing search results, and minimum and maximum values.

### Known limitations
- Money uses `double`, like the earlier weeks.
- Sales figures leave out discounts, delivery and tax, because `Order` still doesn't use `PricingService`.
- If piped input ends in the middle of a menu question, the menu can crash. A person typing can't cause this.