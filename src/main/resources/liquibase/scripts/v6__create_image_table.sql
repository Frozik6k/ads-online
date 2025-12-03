--liquibase formatted sql

--changeset frozik6k:6
CREATE TABLE IF NOT EXISTS image (
    id TEXT PRIMARY KEY,
    path TEXT NOT NULL
)