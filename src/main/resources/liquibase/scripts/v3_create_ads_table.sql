CREATE TABLE extended_ads (
    pk BIGSERIAL PRIMARY KEY,

    author_first_name VARCHAR(50) NOT NULL CHECK (author_first_name ~ '^[А-Яа-яA-Za-z\\-\\s]+$'),
    author_last_name VARCHAR(50) NOT NULL CHECK (author_last_name ~ '^[А-Яа-яA-Za-z\\-\\s]+$'),

    description VARCHAR(1000) NOT NULL CHECK (length(description) >= 10),

    email VARCHAR(255) NOT NULL CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),

    image_url VARCHAR(255) NOT NULL,

    phone VARCHAR(20) NOT NULL CHECK (phone ~ '^\\+?[0-9\\-\\s]{7,15}$'),

    price NUMERIC NOT NULL CHECK (price >= 0),

    title VARCHAR(100) NOT NULL CHECK (length(title) >= 3)
);