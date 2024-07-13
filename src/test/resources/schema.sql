DROP TABLE IF EXISTS profiles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS reaction_emojis CASCADE;
DROP TABLE IF EXISTS reaction_counts CASCADE;
DROP TABLE IF EXISTS reactions CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS votes CASCADE;
DROP TABLE IF EXISTS posts CASCADE;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    uid VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emoji VARCHAR(255),
    nickname VARCHAR(255) NOT NULL,
    sequence BIGINT NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    thumbnail VARCHAR(255),
    is_done BOOLEAN DEFAULT FALSE,
    created_at DATETIME,
    updated_at DATETIME,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE votes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_yes BOOLEAN NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    post_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    post_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE reactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reaction_emoji VARCHAR(50) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    comment_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (comment_id) REFERENCES comments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE reaction_emojis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emoji VARCHAR(10),
    name VARCHAR(50),
    reaction_id BIGINT,
    FOREIGN KEY (reaction_id) REFERENCES reactions(id)
);

CREATE TABLE reaction_counts (
    comment_id BIGINT,
    emoji VARCHAR(50),
    count INT,
    PRIMARY KEY (comment_id, emoji),
    FOREIGN KEY (comment_id) REFERENCES comments(id)
);





