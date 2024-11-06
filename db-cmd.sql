create database cms;
use cms;
CREATE TABLE discounts (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,         -- Discount type identifier
    details JSON NOT NULL,            -- JSON column for discount parameters
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
