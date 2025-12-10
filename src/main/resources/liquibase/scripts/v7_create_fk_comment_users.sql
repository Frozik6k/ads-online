--liquibase formatted sql

--changeset forzik6k:7
ALTER TABLE comment
    ALTER COLUMN users_id SET NOT NULL,
    ADD CONSTRAINT fk_comment_users FOREIGN KEY (users_id) REFERENCES users(id) ON DELETE RESTRICT
