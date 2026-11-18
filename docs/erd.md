# E-commerce ERD (Week 7)

```mermaid
erDiagram
    CUSTOMERS ||--o{ ADDRESSES : "has"
    CUSTOMERS ||--o{ ORDERS : "places"
    CATEGORIES ||--o{ PRODUCTS : "groups"
    ORDERS ||--|{ ORDER_ITEMS : "contains"
    PRODUCTS ||--o{ ORDER_ITEMS : "appears in"
    ORDERS ||--o{ PAYMENTS : "paid by"
    ORDERS ||--o| DELIVERIES : "delivered via"
    ADDRESSES ||--o{ DELIVERIES : "ships to"

    CUSTOMERS {
        text customer_id PK
        text name
        text email UK
        timestamp created_at
    }
    ADDRESSES {
        bigint address_id PK
        text customer_id FK
        text street
        text city
        text state
        text country
        text postal_code
        boolean is_default
    }
    CATEGORIES {
        int category_id PK
        text name UK
    }
    PRODUCTS {
        text product_id PK
        text name
        numeric price
        int stock_quantity
        int category_id FK
    }
    ORDERS {
        text order_id PK
        text customer_id FK
        text status
        text promo_code
        timestamp created_at
    }
    ORDER_ITEMS {
        text order_id PK, FK
        text product_id PK, FK
        int quantity
        numeric unit_price
    }
    PAYMENTS {
        text payment_id PK
        text order_id FK
        numeric amount
        text status
        timestamp created_at
    }
    DELIVERIES {
        text delivery_id PK
        text order_id FK, UK
        bigint address_id FK
        text status
    }
```

## Relationships
- customers to addresses: one-to-many
- customers to orders: one-to-many
- categories to products: one-to-many
- orders to products: **many-to-many**, resolved by `order_items`
- orders to payments: one-to-many (retries)
- orders to deliveries: one-to-one (UNIQUE on `order_id`)
- addresses to deliveries: one-to-many