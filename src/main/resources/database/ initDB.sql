DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    id            uuid PRIMARY KEY UNIQUE,
    first_name     VARCHAR(200) NOT NULL,
    last_name      VARCHAR(200) NOT NULL,
    password      VARCHAR(200) NOT NULL,
    email         VARCHAR(320) NOT NULL UNIQUE,
    local_date_time TIMESTAMP    NOT NULL,
    role          VARCHAR(6)       NOT NULL,
    enabled       BOOLEAN      NOT NULL
);