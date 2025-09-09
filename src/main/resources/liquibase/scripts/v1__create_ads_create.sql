-- liquebase formatted sql

--changeset frozik6k:1
CREATE TABLE comment (
    id INT PRIMARY KEY,
    author_image VARCHAR(255),
    author_firstname VARCHAR(100),
    created_at TIMESTAMP,
    text VARCHAR(64),
    id_author INT
)