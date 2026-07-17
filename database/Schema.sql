--#No Manual Table Setup Required
--#The application automatically creates the bookings table inside your MySQL database booking_system_db on the very first run.
--#You do not need to import any SQL files or write table scripts.
--
-- spring.jpa.hibernate.ddl-auto=update so, no need schema

-- 1. Create and Initialize Database Environment
CREATE DATABASE IF NOT EXISTS booking_system_db;
USE booking_system_db;

-- 2. Drop target table if it already exists for a clean slate
DROP TABLE IF EXISTS bookings;

-- 3. Construct Core Relational Structure
CREATE TABLE bookings (
    booking_no VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,       -- bookingNo/bookingReference
    agent VARCHAR(100) NOT NULL,                  -- Name of the agent
    country VARCHAR(100) NOT NULL,                -- Destination country
    booking_date DATE NOT NULL,                   -- Transaction date
    amount DECIMAL(12, 2) NOT NULL,               -- revenue amount
    tourType VARCHAR(50),                         -- tour type
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' -- State (CONFIRMED, CANCELLED, PENDING)
);

-- 4. Index Optimization Profiles
CREATE INDEX idx_booking_status ON bookings(status);
CREATE INDEX idx_booking_date ON bookings(booking_date);
CREATE INDEX idx_booking_country ON bookings(country);

-- 5. Verification Check
SELECT * FROM bookings;