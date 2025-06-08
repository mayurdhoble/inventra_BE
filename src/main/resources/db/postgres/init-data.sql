-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS roles (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS address_category_master (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS unit_master (
    id VARCHAR(255) PRIMARY KEY,
    unit_code VARCHAR(255) NOT NULL UNIQUE,
    unit_desc VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Initialize Roles
INSERT INTO roles (id, name, description) VALUES
('ROL001', 'ORG_ADMIN', 'Organization Administrator'),
('ROL002', 'BRANCH_ADMIN', 'Branch Administrator')

-- Initialize Address Categories
INSERT INTO address_category_master (id, name, description) VALUES
('ADC001', 'HOME', 'Home Address'),
('ADC002', 'OFFICE', 'Office Address'),
('ADC003', 'MOBILE', 'Mobile Contact'),
('ADC004', 'LANDLINE', 'Landline Contact')

-- Initialize Unit Master
INSERT INTO unit_master (id, unit_code, unit_desc) VALUES
('UNT001', 'METER', 'Product will be measured in meter'),
('UNT002', 'PIECES' , 'Product will be measured in pieces');


