DROP DATABASE IF EXISTS ThogakadeM;
CREATE DATABASE IF NOT EXISTS ThogakadeM;
SHOW DATABASES;
USE ThogakadeM;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer
(
    id      VARCHAR(8),
    name    VARCHAR(30),
    address VARCHAR(30),
    salary  double,
    CONSTRAINT PRIMARY KEY (id)
    );
SHOW TABLES;
DESCRIBE Customer;

DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item
(
    code        VARCHAR(8),
    description VARCHAR(50),
    qty         INT(5)        DEFAULT 0,
    unitPrice   DECIMAL(10, 2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (code)
    );
SHOW TABLES;
DESCRIBE Item;

DROP TABLE IF EXISTS `Orders`;
CREATE TABLE IF NOT EXISTS `Orders`
(
    orderId   VARCHAR(8),
    orderDate DATE,
    cusId     VARCHAR(8),
    CONSTRAINT PRIMARY KEY (orderId, cusId),
    CONSTRAINT FOREIGN KEY (cusId) REFERENCES Customer (id) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES;
DESCRIBE `Orders`;

DROP TABLE IF EXISTS `OrderDetail`;
CREATE TABLE IF NOT EXISTS `OrderDetail`
(
    orderId   VARCHAR(8),
    itemCode  VARCHAR(8),
    qty       INT(5)        DEFAULT 0,
    total DECIMAL(10, 2) DEFAULT 0.00,

    CONSTRAINT PRIMARY KEY (orderId, itemCode),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES `Orders` (orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Item (code) ON DELETE CASCADE ON UPDATE CASCADE

    );
SHOW TABLES;
DESCRIBE `OrderDetail`;

INSERT INTO Customer
VALUES ('C00-001', 'Janith', 'Colombo', 45000),
       ('C00-002', 'Sandaru', 'Galle', 55000),
       ('C00-003', 'Tharindu', 'Colombo', 45000),
       ('C00-004', 'Vijaya', 'Colombo', 75000),
       ('C00-005', 'Ajith', 'Pokunuwita', 45000);

SELECT *
FROM Customer;

INSERT INTO Item
VALUES ('I00-001', 'Sugar', 50, 110.00),
       ('I00-002', 'Samba Rice', 40, 140.00),
       ('I00-003', 'Flour', 30, 80.00),
       ('I00-004', 'Potato', 50, 120.00),
       ('I00-005', 'Onions', 30, 110.00),
       ('I00-006', 'Chocolate Biscuit', 30, 170.00),
       ('I00-007', 'Cream Cracker', 40, 160.00),
       ('I00-008', 'Chicken', 40, 630.00),
       ('I00-009', 'Kakulu Rice',60, 105.00),
       ('I00-010', 'Noodles', 80, 190.00),
       ('I00-011', 'Dhal', 90, 108.00);

SELECT *
FROM Item;

use ThogakadeM;
SELECT orderId FROM `Orders` ORDER BY orderId DESC LIMIT 1