--liquibase formatted sql

--changeset Aidar:1
CREATE TABLE user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(20),
    password VARCHAR(16),
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    email VARCHAR(20),
    role VARCHAR(10),
    phone VARCHAR(20)
)