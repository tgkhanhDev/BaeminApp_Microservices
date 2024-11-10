use baemin_db;

-- Users Table
INSERT INTO "Users" (email, phone_number, first_name, last_name, password, role, is_active)
VALUES
('user1@example.com', '1234567890', 'John', 'Doe', 'password1', 'BUYER', TRUE),
('user2@example.com', '0987654321', 'Jane', 'Smith', 'password2', 'SELLER', TRUE),
('admin@example.com', '1122334455', 'Admin', 'User', 'adminpass', 'ADMIN', TRUE);

-- Poster Table
INSERT INTO "Poster" (poster_thumbnail, status)
VALUES
('https://example.com/poster1.jpg', TRUE),
('https://example.com/poster2.jpg', TRUE),
('https://example.com/poster3.jpg', FALSE);

-- Shop Table
INSERT INTO "Shop" (shop_name, shop_address, shop_thumbnail, category, label, location, rating, open_time, close_time, price_start, price_end, is_open)
VALUES
('Cafe Good', '123 Main St', 'https://example.com/shop1.jpg', 'Coffee', 'Food', 'Ho Chi Minh', 4.5, '08:00', '22:00', 50, 150, TRUE),
('Spicy Corner', '456 Oak St', 'https://example.com/shop2.jpg', 'Restaurant', 'Food', 'Ha Noi', 4.7, '10:00', '23:00', 100, 300, TRUE),
('Dessert Kingdom', '789 Pine St', 'https://example.com/shop3.jpg', 'Dessert', 'Dessert', 'Da Nang', 4.8, '09:00', '20:00', 30, 100, TRUE);

-- Food Table
INSERT INTO "Food" (food_name, description, price, type, shop_id)
VALUES
('Latte', 'A rich, creamy coffee drink', 50, 'combo', (SELECT shop_id FROM "Shop" WHERE shop_name = 'Cafe Good' LIMIT 1)),
('Pho', 'Traditional Vietnamese noodle soup', 150, 'sale', (SELECT shop_id FROM "Shop" WHERE shop_name = 'Spicy Corner' LIMIT 1)),
('Chocolate Cake', 'Delicious and moist chocolate cake', 80, 'none', (SELECT shop_id FROM "Shop" WHERE shop_name = 'Dessert Kingdom' LIMIT 1));

-- CartItem Table
INSERT INTO "CartItem" (account_id, food_id, quantity)
VALUES
((SELECT user_id FROM "Users" WHERE email = 'user1@example.com' LIMIT 1), (SELECT food_id FROM "Food" WHERE food_name = 'Latte' LIMIT 1), 2),
((SELECT user_id FROM "Users" WHERE email = 'user2@example.com' LIMIT 1), (SELECT food_id FROM "Food" WHERE food_name = 'Pho' LIMIT 1), 1),
((SELECT user_id FROM "Users" WHERE email = 'user1@example.com' LIMIT 1), (SELECT food_id FROM "Food" WHERE food_name = 'Chocolate Cake' LIMIT 1), 3);

-- Payment Table
INSERT INTO "Payment" (delivery_address, message, total_cost, status)
VALUES
('123 Main St, Ho Chi Minh', 'Please deliver quickly', 200, 'Paid'),
('456 Oak St, Ha Noi', 'Add extra spice', 350, 'Unpaid'),
('789 Pine St, Da Nang', 'No delivery needed', 240, 'Paid');

-- Transaction Table
INSERT INTO "Transaction" (food_id, quantity, per_price, payment_id, status)
VALUES
((SELECT food_id FROM "Food" WHERE food_name = 'Latte' LIMIT 1), 2, 50, (SELECT payment_id FROM "Payment" WHERE status = 'Paid' LIMIT 1), 'complete'),
((SELECT food_id FROM "Food" WHERE food_name = 'Pho' LIMIT 1), 1, 150, (SELECT payment_id FROM "Payment" WHERE status = 'Unpaid' LIMIT 1), 'not started'),
((SELECT food_id FROM "Food" WHERE food_name = 'Chocolate Cake' LIMIT 1), 3, 80, (SELECT payment_id FROM "Payment" WHERE status = 'Paid' LIMIT 1), 'in progress');
