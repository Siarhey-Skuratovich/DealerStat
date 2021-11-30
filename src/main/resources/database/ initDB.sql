DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY UNIQUE,
    first_name VARCHAR(200) NOT NULL,
    last_name  VARCHAR(200) NOT NULL,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(320) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL,
    role       VARCHAR(6)   NOT NULL,
    enabled    BOOLEAN      NOT NULL
);

DROP TABLE IF EXISTS comments;
CREATE TABLE IF NOT EXISTS comments
(
    id         UUID PRIMARY KEY,
    message    VARCHAR(500) NOT NULL,
    post_id    UUID         NOT NULL,
    author_id  UUID         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    approved   BOOLEAN      NOT NULL
)