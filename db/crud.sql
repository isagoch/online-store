-- CRUD queries for FlexiSA Week 7. Run right after seed.sql.

-- 1. INSERT: new customer
INSERT INTO customers (customer_id, name, email)
VALUES ('C008', 'Peter James', 'peter.james@example.com');

-- 2. INSERT: address for that customer
INSERT INTO addresses (customer_id, street, city, state, postal_code, is_default)
VALUES ('C008', '6 Airport Road', 'Abuja', 'FCT', '900003', TRUE)
RETURNING address_id, customer_id;

-- 3. INSERT: new order (status comes from its DEFAULT)
INSERT INTO orders (order_id, customer_id)
VALUES ('O1007', 'C008')
RETURNING order_id, status;

-- 4. INSERT: order line (junction table)
INSERT INTO order_items (order_id, product_id, quantity, unit_price)
VALUES ('O1007', 'P002', 2, 8500);

-- 5. SELECT: all orders with customer name (JOIN)
SELECT o.order_id, c.name, o.status
FROM orders o JOIN customers c USING (customer_id)
ORDER BY o.order_id;

-- 6. SELECT: order totals (GROUP BY)
SELECT order_id, sum(quantity * unit_price) AS total
FROM order_items GROUP BY order_id ORDER BY order_id;

-- 7. SELECT: products in the Accessories category (JOIN + WHERE)
SELECT p.product_id, p.name, c.name AS category
FROM products p JOIN categories c USING (category_id)
WHERE c.name = 'Accessories' ORDER BY p.product_id;

-- 8. SELECT: many-to-many, products on order O1006
SELECT o.order_id, p.name AS product, oi.quantity, oi.unit_price
FROM orders o
JOIN order_items oi ON oi.order_id = o.order_id
JOIN products p ON p.product_id = oi.product_id
WHERE o.order_id = 'O1006' ORDER BY p.product_id;

-- 9. SELECT: orders with more than one payment attempt (one-to-many)
SELECT order_id, count(*) AS payment_attempts
FROM payments GROUP BY order_id HAVING count(*) > 1;

-- 10. UPDATE: move order O1004 forward
UPDATE orders SET status = 'PROCESSING'
WHERE order_id = 'O1004' RETURNING order_id, status;

-- 11. UPDATE: restock the USB-C Hub
UPDATE products SET stock_quantity = stock_quantity + 10
WHERE product_id = 'P004' RETURNING product_id, stock_quantity;

-- 12. DELETE: remove order O1007 (its order_items rows go too, via ON DELETE CASCADE)
DELETE FROM orders WHERE order_id = 'O1007';
SELECT count(*) AS items_left_for_o1007 FROM order_items WHERE order_id = 'O1007';

-- 13. DELETE: remove customer C008 (its address goes too, via ON DELETE CASCADE)
DELETE FROM customers WHERE customer_id = 'C008';
SELECT (SELECT count(*) FROM customers) AS customers,
        (SELECT count(*) FROM orders) AS orders,
        (SELECT count(*) FROM order_items) AS order_items,
        (SELECT count(*) FROM addresses) AS addresses;