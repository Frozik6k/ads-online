--liquibase formatted sql

--changeset Aidar:2
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(20),
    password VARCHAR(16),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    email VARCHAR(20),
    role VARCHAR(10),
    image VARCHAR(255),
    phone VARCHAR(20)
)