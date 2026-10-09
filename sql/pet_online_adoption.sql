-- Online Pet Adoption Platform - schema + sample data
-- WARNING: this script DROPS and recreates the pet_adoption database,
-- so re-running it resets everything to the sample data below.

DROP DATABASE IF EXISTS pet_adoption;
CREATE DATABASE pet_adoption;
USE pet_adoption;

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    contact VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    CONSTRAINT chk_user_role CHECK (role IN ('Admin', 'Shelter', 'Adopter'))
);

CREATE TABLE Pets (
    pet_id INT PRIMARY KEY AUTO_INCREMENT,
    shelter_user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    age INT,
    description TEXT,
    photo VARCHAR(255),
    listing_status VARCHAR(20) DEFAULT 'Pending',
    pet_status VARCHAR(20) DEFAULT 'Available',
    FOREIGN KEY (shelter_user_id) REFERENCES Users(user_id),
    CONSTRAINT chk_pet_age CHECK (age IS NULL OR age >= 0),
    CONSTRAINT chk_listing_status CHECK (listing_status IN ('Pending', 'Approved', 'Rejected')),
    CONSTRAINT chk_pet_status CHECK (pet_status IN ('Available', 'Adopted'))
);

CREATE TABLE AdoptionApplications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    adopter_id INT NOT NULL,
    pet_id INT NOT NULL,
    application_details TEXT,
    application_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (adopter_id) REFERENCES Users(user_id),
    FOREIGN KEY (pet_id) REFERENCES Pets(pet_id),
    CONSTRAINT uq_adopter_pet UNIQUE (adopter_id, pet_id),
    CONSTRAINT chk_application_status CHECK (status IN ('Pending', 'Approved', 'Rejected'))
);

CREATE TABLE Messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    send_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    delivery_status VARCHAR(20) DEFAULT 'Sent',
    FOREIGN KEY (sender_id) REFERENCES Users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES Users(user_id),
    CONSTRAINT chk_delivery_status CHECK (delivery_status IN ('Sent', 'Delivered', 'Read'))
);

CREATE TABLE Settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    setting_name VARCHAR(100) UNIQUE NOT NULL,
    setting_value VARCHAR(255)
);

-- ---------- sample data ----------
INSERT INTO Users (name, email, password, role, contact, address, city, state) VALUES
    ('Admin user', 'admin@petadoption.com', 'admin123', 'Admin', '9876543210', 'Platform Office', 'Delhi', 'Delhi'),
    ('Happy Paws Shelter', 'shelter@petadoption.com', 'shelter123', 'Shelter', '9876543211', '12 Park Road', 'Noida', 'UttarPradesh'),
    ('Adopter User', 'adopter@petadoption.com', 'adopter123', 'Adopter', '9876543212', '45 Green Street', 'Delhi', 'Delhi');

INSERT INTO Pets (shelter_user_id, name, type, breed, age, description, photo, listing_status, pet_status) VALUES
    (2, 'Max', 'Dog', 'Labrador', 3, 'Friendly and energetic Labrador looking for a loving home.', 'images/max.jpg', 'Approved', 'Available'),
    (2, 'Bella', 'Cat', 'Persian', 2, 'Calm and affectionate Persian cat.', 'images/bella.jpg', 'Approved', 'Available');

INSERT INTO AdoptionApplications (adopter_id, pet_id, application_details, status) VALUES
    (3, 1, 'I would like to adopt Max and provide a loving home.', 'Pending');

INSERT INTO Messages (sender_id, receiver_id, message_text, delivery_status) VALUES
    (2, 3, 'Hello! Thank you for your interest in adopting Max.', 'Sent'),
    (3, 2, 'Thank you! I would love to know more about Max.', 'Sent');

INSERT INTO Settings (setting_name, setting_value) VALUES
    ('PlatformName', 'Online Pet Adoption Platform'),
    ('ApplicationReviewTime', '3 Days'),
    ('MaintenanceMode', 'OFF');
