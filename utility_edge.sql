CREATE DATABASE smart_utility;

USE smart_utility;

CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    phone VARCHAR(15),
    address VARCHAR(255),
    meter_no VARCHAR(20),
    username VARCHAR(50),
    password VARCHAR(50)
);

CREATE TABLE bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    month VARCHAR(20),
    electricity_units INT,
    water_units INT,
    gas_units INT,
    internet_charge DOUBLE,
    total_amount DOUBLE,
    status VARCHAR(20),
    due_date DATE,
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

CREATE TABLE admin (
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO admin VALUES ('admin', 'admin123');