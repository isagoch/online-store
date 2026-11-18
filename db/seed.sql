-- Seed data for FlexiSA Week 7. Safe to re-run: clears tables first.
TRUNCATE deliveries, payments, order_items, orders, products,
         categories, addresses, customers RESTART IDENTITY CASCADE;

INSERT INTO customers (customer_id, name, email) VALUES
('C001','Ada Okafor','ada.okafor@example.com'),
('C002','Tunde Bello','tunde.bello@example.com'),
('C003','Chika Nwosu','chika.nwosu@example.com'),
('C004','Musa Ibrahim','musa.ibrahim@example.com'),
('C005','Grace Eze','grace.eze@example.com'),
('C006','Daniel Afolabi','daniel.afolabi@example.com'),
('C007','Zainab Yusuf','zainab.yusuf@example.com');

-- address_id is generated: 1..8 in this order
INSERT INTO addresses (customer_id, street, city, state, postal_code, is_default) VALUES
('C001','14 Allen Avenue','Ikeja','Lagos','100001',TRUE),
('C002','8 Ring Road','Ibadan','Oyo','200212',TRUE),
('C003','22 Aminu Kano Crescent','Abuja','FCT','900001',TRUE),
('C003','9 Garki Road','Abuja','FCT','900002',FALSE),
('C004','5 Zoo Road','Kano','Kano','700001',TRUE),
('C005','17 Aba Road','Port Harcourt','Rivers','500001',TRUE),
('C006','3 Broad Street','Lagos','Lagos','100002',TRUE),
('C007','11 Dugbe Road','Ibadan','Oyo','200213',TRUE);

INSERT INTO categories (name) VALUES
('Computers'),('Accessories'),('Displays'),('Office');

INSERT INTO products (product_id, name, price, stock_quantity, category_id) VALUES
('P001','Laptop',450000,12,(SELECT category_id FROM categories WHERE name='Computers')),
('P002','Wireless Mouse',8500,45,(SELECT category_id FROM categories WHERE name='Accessories')),
('P003','Mechanical Keyboard',32000,18,(SELECT category_id FROM categories WHERE name='Accessories')),
('P004','USB-C Hub',14500,0,(SELECT category_id FROM categories WHERE name='Accessories')),
('P005','Monitor',185000,7,(SELECT category_id FROM categories WHERE name='Displays')),
('P006','Desk Lamp',12500,24,(SELECT category_id FROM categories WHERE name='Office')),
('P007','Webcam',27000,9,(SELECT category_id FROM categories WHERE name='Accessories')),
('P008','Headphones',42000,15,(SELECT category_id FROM categories WHERE name='Accessories')),
('P009','External SSD',68000,6,(SELECT category_id FROM categories WHERE name='Computers'));

INSERT INTO orders (order_id, customer_id, status, promo_code, created_at) VALUES
('O1001','C001','DELIVERED',NULL,'2026-10-01 09:15:00'),
('O1002','C002','SHIPPED',NULL,'2026-10-05 14:30:00'),
('O1003','C003','PROCESSING',NULL,'2026-10-12 11:00:00'),
('O1004','C004','PENDING',NULL,'2026-10-20 16:45:00'),
('O1005','C005','CANCELLED',NULL,'2026-10-22 10:20:00'),
('O1006','C001','PENDING','FLEXISAF','2026-11-02 08:05:00');

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
('O1001','P001',1,450000),('O1001','P002',2,8500),
('O1002','P005',1,185000),('O1002','P006',2,12500),
('O1003','P003',1,32000),('O1003','P008',1,42000),
('O1004','P009',1,68000),
('O1005','P007',2,27000),
('O1006','P002',3,8500),('O1006','P006',1,12500);

INSERT INTO payments (payment_id, order_id, amount, status) VALUES
('PAY001','O1001',467000,'COMPLETED'),
('PAY002','O1002',210000,'COMPLETED'),
('PAY003','O1003',74000,'FAILED'),
('PAY004','O1003',74000,'COMPLETED'),
('PAY005','O1005',54000,'REFUNDED');

INSERT INTO deliveries (delivery_id, order_id, address_id, status) VALUES
('D001','O1001',1,'DELIVERED'),
('D002','O1002',2,'IN_TRANSIT'),
('D003','O1003',4,'PENDING');