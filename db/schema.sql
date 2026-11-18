-- E-commerce schema for FlexiSA Week 7 (PostgreSQL 18)
-- Rebuildable: drops everything first, then recreates.

DROP TABLE IF EXISTS deliveries   CASCADE;
DROP TABLE IF EXISTS payments     CASCADE;
DROP TABLE IF EXISTS order_items  CASCADE;
DROP TABLE IF EXISTS orders       CASCADE;
DROP TABLE IF EXISTS products     CASCADE;
DROP TABLE IF EXISTS categories   CASCADE;
DROP TABLE IF EXISTS addresses    CASCADE;
DROP TABLE IF EXISTS customers    CASCADE;

CREATE TABLE customers (
    customer_id TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    email       TEXT NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT ck_customer_id_not_blank CHECK (btrim(customer_id) <> ''),
    CONSTRAINT ck_customer_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT ck_customer_email_has_at CHECK (email LIKE '%@%')
);

-- One customer has many addresses (one-to-many)
CREATE TABLE addresses (
    address_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id TEXT NOT NULL REFERENCES customers(customer_id) ON DELETE CASCADE,
    street      TEXT NOT NULL,
    city        TEXT NOT NULL,
    state       TEXT NOT NULL,
    country     TEXT NOT NULL DEFAULT 'Nigeria',
    postal_code TEXT NOT NULL,
    is_default  BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT ck_address_street_not_blank CHECK (btrim(street) <> ''),
    CONSTRAINT ck_address_city_not_blank CHECK (btrim(city) <> ''),
    CONSTRAINT ck_address_state_not_blank CHECK (btrim(state) <> ''),
    CONSTRAINT ck_address_postal_not_blank CHECK (btrim(postal_code) <> '')
);

CREATE TABLE categories (
    category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        TEXT NOT NULL UNIQUE,
    CONSTRAINT ck_category_name_not_blank CHECK (btrim(name) <> '')
);

CREATE TABLE products (
    product_id     TEXT PRIMARY KEY,
    name           TEXT NOT NULL,
    price          NUMERIC(12,2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category_id    INT REFERENCES categories(category_id),
    CONSTRAINT ck_product_id_not_blank CHECK (btrim(product_id) <> ''),
    CONSTRAINT ck_product_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT ck_product_price_positive CHECK (price > 0),
    CONSTRAINT ck_product_stock_not_negative CHECK (stock_quantity >= 0)
);

CREATE TABLE orders (
    order_id    TEXT PRIMARY KEY,
    customer_id TEXT NOT NULL REFERENCES customers(customer_id),
    status      TEXT NOT NULL DEFAULT 'PENDING',
    promo_code  TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT ck_order_id_not_blank CHECK (btrim(order_id) <> ''),
    CONSTRAINT ck_order_status CHECK (status IN
        ('PENDING','PROCESSING','SHIPPED','DELIVERED','CANCELLED'))
);

-- Junction table: orders <-> products is many-to-many
CREATE TABLE order_items (
    order_id   TEXT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id TEXT NOT NULL REFERENCES products(product_id),
    quantity   INT NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL,   -- price at time of purchase
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT ck_item_quantity_positive CHECK (quantity >= 1),
    CONSTRAINT ck_item_price_positive CHECK (unit_price > 0)
);

-- One order may have several payment attempts (one-to-many)
CREATE TABLE payments (
    payment_id TEXT PRIMARY KEY,
    order_id   TEXT NOT NULL REFERENCES orders(order_id),
    amount     NUMERIC(12,2) NOT NULL,
    status     TEXT NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT ck_payment_id_not_blank CHECK (btrim(payment_id) <> ''),
    CONSTRAINT ck_payment_amount_positive CHECK (amount > 0),
    CONSTRAINT ck_payment_status CHECK (status IN
        ('PENDING','PROCESSING','COMPLETED','FAILED','REFUNDED'))
);

-- One order has at most one delivery (one-to-one, enforced by UNIQUE)
CREATE TABLE deliveries (
    delivery_id TEXT PRIMARY KEY,
    order_id    TEXT NOT NULL UNIQUE REFERENCES orders(order_id),
    address_id  BIGINT NOT NULL REFERENCES addresses(address_id),
    status      TEXT NOT NULL DEFAULT 'PENDING',
    CONSTRAINT ck_delivery_id_not_blank CHECK (btrim(delivery_id) <> ''),
    CONSTRAINT ck_delivery_status CHECK (status IN
        ('PENDING','IN_TRANSIT','DELIVERED','RETURNED'))
);