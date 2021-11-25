DROP TABLE IF EXISTS userEntities;
CREATE TABLE IF NOT EXISTS userEntities
(
    id            uuid PRIMARY KEY,
    first_name     VARCHAR(200) NOT NULL,
    last_name      VARCHAR(200) NOT NULL,
    password      VARCHAR(200) NOT NULL,
    email         VARCHAR(320) NOT NULL,
    local_date_time TIMESTAMP    NOT NULL,
    role          VARCHAR(6)       NOT NULL,
    enabled       BOOLEAN      NOT NULL
);