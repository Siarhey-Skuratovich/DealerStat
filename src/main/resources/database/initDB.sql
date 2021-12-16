DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users
(
    user_id    UUID PRIMARY KEY,
    first_name VARCHAR(200) NOT NULL,
    last_name  VARCHAR(200) NOT NULL,
    password   VARCHAR(200) NOT NULL,
    email      VARCHAR(320) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL,
    role       VARCHAR(6)   NOT NULL,
    enabled    BOOLEAN      NOT NULL
);

DROP TABLE IF EXISTS posts CASCADE;
CREATE TABLE IF NOT EXISTS posts
(
    post_id           UUID PRIMARY KEY,
    trader_id         UUID          NOT NULL,
    trader_first_name VARCHAR(200)  NOT NULL,
    trader_last_name  VARCHAR(200)  NOT NULL,
    author_id         UUID,
    text              VARCHAR(1000) NOT NULL,
    approved          BOOLEAN       NOT NULL,

    FOREIGN KEY (trader_id) REFERENCES users (user_id) ON UPDATE CASCADE
);

DROP TABLE IF EXISTS comments CASCADE;
CREATE TABLE IF NOT EXISTS comments
(
    comment_id UUID PRIMARY KEY,
    message    VARCHAR(500) NOT NULL,
    post_id    UUID         NOT NULL,
    author_id  UUID         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    approved   BOOLEAN      NOT NULL,

    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON UPDATE CASCADE
);

DROP TABLE IF EXISTS games CASCADE;
CREATE TABLE IF NOT EXISTS games
(
    game_id   UUID PRIMARY KEY,
    game_name VARCHAR(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS posts_games CASCADE;
CREATE TABLE IF NOT EXISTS posts_games
(
    post_id UUID,
    game_id UUID,
    PRIMARY KEY (post_id, game_id),
    FOREIGN KEY (post_id) REFERENCES posts (post_id),
    FOREIGN KEY (game_id) REFERENCES games (game_id)
);

DROP TABLE IF EXISTS game_objects CASCADE;
CREATE TABLE IF NOT EXISTS game_objects
(
    game_object_id UUID PRIMARY KEY,
    title          VARCHAR(50)  NOT NULL,
    text           VARCHAR(200) NOT NULL,
    status         VARCHAR(9)   NOT NULL,
    author_id      UUID         NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    game_id        UUID         NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games (game_id) ON UPDATE CASCADE
);

DROP TABLE IF EXISTS posts_game_objects CASCADE;
CREATE TABLE IF NOT EXISTS posts_game_objects
(
    post_id        UUID,
    game_object_id UUID,
    PRIMARY KEY (post_id, game_object_id),
    FOREIGN KEY (post_id) REFERENCES posts (post_id) ON UPDATE CASCADE,
    FOREIGN KEY (game_object_id) REFERENCES game_objects (game_object_id) ON UPDATE CASCADE
);



