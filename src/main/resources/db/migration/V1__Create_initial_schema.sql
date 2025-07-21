CREATE TABLE unit (
    name VARCHAR(100) PRIMARY KEY,
    attack INT NOT NULL,
    defense INT NOT NULL,
    shots INT,
    min_damage INT NOT NULL,
    max_damage INT NOT NULL,
    hp INT NOT NULL,
    speed INT NOT NULL,
    description VARCHAR(255),
    image_path VARCHAR(255)
);

CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    creation_date TIMESTAMP NOT NULL,
    expiration_date TIMESTAMP NOT NULL
);

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    refresh_token_id BIGINT UNIQUE,
    CONSTRAINT fk_user_refresh_token FOREIGN KEY (refresh_token_id) REFERENCES refresh_token(id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    -- Klucz podstawowy zapobiegający duplikatom (ten sam user nie może mieć tej samej roli dwa razy)
    PRIMARY KEY (user_id, role),
    -- Klucz obcy wskazujący na użytkownika
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);