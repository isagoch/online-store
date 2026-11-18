-- Constraint tests: each statement below must be REJECTED.

-- 1. CHECK: negative price
INSERT INTO products (product_id, name, price, stock_quantity)
VALUES ('P010', 'Office Chair', -75000, 4);

-- 2. PRIMARY KEY: duplicate customer ID
INSERT INTO customers (customer_id, name, email)
VALUES ('C003', 'Chika Nwosu', 'other@example.com');

-- 3. CHECK: email without @
INSERT INTO customers (customer_id, name, email)
VALUES ('C009', 'Peter James', 'peter.james.example.com');

-- 4. CHECK: blank name
INSERT INTO customers (customer_id, name, email)
VALUES ('C010', '', 'blank.name@example.com');

-- 5. NOT NULL: missing postal code
INSERT INTO addresses (customer_id, street, city, state)
VALUES ('C006', '4 Osborne Road', 'Lagos', 'Lagos');

-- 6. FOREIGN KEY: order for a customer that does not exist
INSERT INTO orders (order_id, customer_id) VALUES ('O9999', 'C999');

-- 7. CHECK: invalid order status
UPDATE orders SET status = 'SHIPPING_SOON' WHERE order_id = 'O1001';

-- 8. UNIQUE: second delivery for the same order
INSERT INTO deliveries (delivery_id, order_id, address_id, status)
VALUES ('D004', 'O1001', 1, 'PENDING');

-- 9. CHECK: negative stock
UPDATE products SET stock_quantity = -1 WHERE product_id = 'P001';

-- 10. FOREIGN KEY: delete a customer who still has orders
DELETE FROM customers WHERE customer_id = 'C004';