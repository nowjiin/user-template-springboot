-- Initial database setup script
-- This script will create an initial admin user

-- Create the users table if it doesn't exist (though Spring Boot will handle this)
-- CREATE TABLE IF NOT EXISTS users (
--     id SERIAL PRIMARY KEY,
--     username VARCHAR(50) UNIQUE NOT NULL,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     role VARCHAR(20) NOT NULL DEFAULT 'USER',
--     enabled BOOLEAN NOT NULL DEFAULT TRUE,
--     account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
--     account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
--     credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Insert initial admin user
-- Password is 'admin123' hashed with BCrypt
INSERT INTO users (username, email, password, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) 
VALUES (
    'admin', 
    'admin@example.com', 
    '$2a$10$8K4QzL8vgLqS1/Vt1HhQPO5I4gzT8s1jRWh6Oj7pKf5YqJGvMwP72', 
    'ADMIN', 
    true, 
    true, 
    true, 
    true, 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- Insert initial regular user
-- Password is 'user123' hashed with BCrypt
INSERT INTO users (username, email, password, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) 
VALUES (
    'user', 
    'user@example.com', 
    '$2a$10$vKJ7hFqJ6Sh0pB0pKM2X5u5rQJl7T8z1qRWg9Kl7fG5XqMNvOxQ82', 
    'USER', 
    true, 
    true, 
    true, 
    true, 
    CURRENT_TIMESTAMP, 
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING; 