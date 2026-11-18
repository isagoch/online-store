# Normalisation and design decisions (Week 7)

## Normalisation note
- **1NF:** every column holds one atomic value, and every table has a primary key. The Java `Customer` embedded an `Address` object, so it became a separate `addresses` table.
- **2NF:** `order_items` has a composite key (`order_id`, `product_id`). Its non-key columns (`quantity`, `unit_price`) depend on the whole key. Product name and category are not repeated there, only in `products`.
- **3NF:** category names live once in `categories` instead of being repeated on every product. Customer name and email live once in `customers` and are not copied onto orders. The delivery address is referenced by `address_id` instead of being copied into each delivery.
- **Deliberate denormalisation:** `order_items.unit_price` copies the product price at purchase time. That is intentional, so later price changes don't rewrite order history.

## Design decisions
| Decision | Choice | Why |
|---|---|---|
| Money | `NUMERIC(12,2)` | Java `double` loses precision on money |
| Status columns | `TEXT` + `CHECK` | Easier to change than a PostgreSQL ENUM; values match the Java enums exactly |
| IDs | Text business keys (C001, P001, O1001) | Match the Week 6 CSV data |
| Address ids | Generated identity | Addresses have no natural business key |
| Payments | One order, many payments | A failed attempt can be retried (O1003 in the seed) |
| Deliveries | One per order via UNIQUE | Matches the Java model |

## Known limitations
- `order_items` has a composite key, so the same product appears once per order. The Java `Order` allows the same product to be added twice as separate items. In SQL it would be one line with the combined quantity.
- A delivery's address is not forced to belong to the order's customer.
- Payment amounts in the seed equal the item subtotal. Delivery fees, tax and discounts are not stored (the Week 1 to 3 pricing rules aren't modelled).
- Seed prices come from `products.csv`, not the Week 6 sample orders, because those disagree.
- `Order` has no date in the Java model, so `created_at` exists only in SQL.