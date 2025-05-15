-- Initialize database for LaptopStoreConsoleApp

-- Create tables if they don't exist
-- Users/Accounts Table
CREATE TABLE IF NOT EXISTS accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255)
);

-- Laptops Table
CREATE TABLE IF NOT EXISTS laptops (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    cpu VARCHAR(50) NOT NULL,
    ram INT NOT NULL,
    os VARCHAR(50) NOT NULL,
    color VARCHAR(30) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0
);

-- Orders Table
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES accounts(id)
);

-- Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    laptop_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (laptop_id) REFERENCES laptops(id)
);

-- Import Receipts Table
CREATE TABLE IF NOT EXISTS import_receipts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    laptop_id INT NOT NULL,
    quantity INT NOT NULL,
    import_date TIMESTAMP NOT NULL,
    supplier_name VARCHAR(100) NOT NULL,
    supplier_contact VARCHAR(100),
    notes TEXT,
    FOREIGN KEY (laptop_id) REFERENCES laptops(id)
);

-- Warranty Requests Table
CREATE TABLE IF NOT EXISTS warranty_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    laptop_id INT NOT NULL,
    user_id INT NOT NULL,
    request_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    reason TEXT,
    admin_notes TEXT,
    processed_date TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (laptop_id) REFERENCES laptops(id),
    FOREIGN KEY (user_id) REFERENCES accounts(id)
);

-- Insert sample data
-- Sample accounts (password is in plain text as per requirements)
INSERT INTO accounts (username, password, role, full_name, email, phone, address)
VALUES 
    ('admin', 'admin123', 'ADMIN', 'System Administrator', 'admin@laptopstore.com', '123-456-7890', '123 Admin St, Admin City'),
    ('john', 'pass123', 'USER', 'John Doe', 'john@example.com', '987-654-3210', '456 Customer Ave, User Town'),
    ('jane', 'jane123', 'USER', 'Jane Smith', 'jane@example.com', '555-123-4567', '789 User Blvd, Customer City');

-- Sample laptops
INSERT INTO laptops (name, brand, cpu, ram, os, color, price, stock_quantity)
VALUES 
    ('XPS 13', 'Dell', 'Intel Core i7-1165G7', 16, 'Windows 11', 'Silver', 1299.99, 10),
    ('MacBook Pro 13', 'Apple', 'Apple M1', 8, 'macOS', 'Space Gray', 1299.99, 15),
    ('ThinkPad X1 Carbon', 'Lenovo', 'Intel Core i5-1135G7', 8, 'Windows 11', 'Black', 1149.99, 8),
    ('ROG Zephyrus G14', 'Asus', 'AMD Ryzen 9 5900HS', 16, 'Windows 11', 'White', 1499.99, 5),
    ('Surface Laptop 4', 'Microsoft', 'AMD Ryzen 5', 8, 'Windows 11', 'Platinum', 999.99, 12),
    ('Gram 17', 'LG', 'Intel Core i7-1165G7', 16, 'Windows 11', 'Black', 1499.99, 7),
    ('Swift 5', 'Acer', 'Intel Core i5-1135G7', 8, 'Windows 11', 'Blue', 899.99, 9),
    ('Spectre x360', 'HP', 'Intel Core i7-1165G7', 16, 'Windows 11', 'Silver', 1399.99, 6),
    ('Galaxy Book Pro', 'Samsung', 'Intel Core i5-1135G7', 8, 'Windows 11', 'Blue', 1099.99, 11),
    ('Yoga 9i', 'Lenovo', 'Intel Core i7-1185G7', 16, 'Windows 11', 'Shadow Black', 1379.99, 4);

-- Sample orders
INSERT INTO orders (user_id, order_date, status, total_amount, payment_method, payment_status, shipping_address)
VALUES 
    (2, '2025-04-15 14:30:00', 'DELIVERED', 1299.99, 'CREDIT_CARD', 'PAID', '456 Customer Ave, User Town'),
    (3, '2025-04-25 10:15:00', 'SHIPPED', 1149.99, 'BANK_TRANSFER', 'PAID', '789 User Blvd, Customer City'),
    (2, '2025-05-01 09:45:00', 'PENDING', 899.99, 'CASH_ON_DELIVERY', 'UNPAID', '456 Customer Ave, User Town');

-- Sample order items
INSERT INTO order_items (order_id, laptop_id, quantity, unit_price)
VALUES 
    (1, 2, 1, 1299.99),
    (2, 3, 1, 1149.99),
    (3, 7, 1, 899.99);

-- Sample import receipts
INSERT INTO import_receipts (laptop_id, quantity, import_date, supplier_name, supplier_contact, notes)
VALUES 
    (1, 10, '2025-03-01 09:00:00', 'Dell Distributors', 'contact@delldist.com', 'Initial stock'),
    (2, 15, '2025-03-01 10:30:00', 'Apple Resellers', 'sales@appleresell.com', 'Initial stock'),
    (3, 8, '2025-03-02 11:45:00', 'Lenovo Suppliers', 'orders@lenovosupply.com', 'Initial stock'),
    (4, 5, '2025-03-03 14:00:00', 'Asus Direct', 'supply@asusdirect.com', 'Initial stock'),
    (5, 12, '2025-03-04 15:30:00', 'Microsoft Store', 'wholesale@microsoft.com', 'Initial stock'),
    (6, 7, '2025-03-05 09:15:00', 'LG Electronics', 'business@lg.com', 'Initial stock'),
    (7, 9, '2025-03-06 10:45:00', 'Acer Distributors', 'sales@acerdist.com', 'Initial stock'),
    (8, 6, '2025-03-07 13:30:00', 'HP Suppliers', 'orders@hpsuppliers.com', 'Initial stock'),
    (9, 11, '2025-03-08 11:00:00', 'Samsung Electronics', 'b2b@samsung.com', 'Initial stock'),
    (10, 4, '2025-03-09 14:45:00', 'Lenovo Suppliers', 'orders@lenovosupply.com', 'Initial stock');

-- Sample warranty requests
INSERT INTO warranty_requests (order_id, laptop_id, user_id, request_date, status, reason, admin_notes, processed_date)
VALUES 
    (1, 2, 2, '2025-04-30 16:45:00', 'APPROVED', 'Battery not charging properly', 'Confirmed battery issue. Replacement sent.', '2025-05-02 10:30:00'),
    (2, 3, 3, '2025-05-05 11:30:00', 'PENDING', 'Screen has dead pixels', NULL, NULL);