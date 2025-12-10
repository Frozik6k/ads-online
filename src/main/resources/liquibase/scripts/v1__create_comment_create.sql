--liquibase formatted sql

--changeset frozik6k:1
CREATE TABLE IF NOT EXISTS comment (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    text VARCHAR(64),
    ad_id BIGINT NOT NULL,
    users_id BIGINT NOT NULL
)