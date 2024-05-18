CREATE TABLE IF NOT EXISTS users
(
    id                     UUID PRIMARY KEY,
    email                  VARCHAR(50)  NOT NULL UNIQUE,
    username               VARCHAR(50)  NOT NULL UNIQUE,
    password               VARCHAR(255) NOT NULL,
    enabled                BOOLEAN,
    avatar                 VARCHAR(255),
    allow_notifications    BOOLEAN      NOT NULL,
    notification_interval  BIGINT       NOT NULL,
    last_notification_sent TIMESTAMP,
    created_by             UUID REFERENCES users (id),
    last_modified_by       UUID REFERENCES users (id),
    created_at             TIMESTAMP,
    last_modified_at       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS workouts
(
    id               UUID PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    thumbnail        VARCHAR(255),
    channel_title    VARCHAR(255) NOT NULL,
    difficulty_level VARCHAR(255) NOT NULL,
    exercise_type    VARCHAR(255) NOT NULL,
    created_by       UUID REFERENCES users (id),
    last_modified_by UUID REFERENCES users (id),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS recipes
(
    id               UUID PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    instructions     TEXT         NOT NULL,
    created_by       UUID REFERENCES users (id),
    last_modified_by UUID REFERENCES users (id),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ingredients
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    calories_per_unit DOUBLE PRECISION,
    created_by        UUID REFERENCES users (id),
    last_modified_by  UUID REFERENCES users (id),
    created_at        TIMESTAMP,
    last_modified_at  TIMESTAMP
);


CREATE TABLE IF NOT EXISTS recipe_ingredients
(
    id               UUID PRIMARY KEY,
    quantity         DOUBLE PRECISION NOT NULL,
    unit             VARCHAR(50) NOT NULL,
    recipe_id        UUID REFERENCES recipes (id) ON DELETE CASCADE,
    ingredient_id    UUID REFERENCES ingredients (id) ON DELETE CASCADE,
    created_by       UUID REFERENCES users (id),
    last_modified_by UUID REFERENCES users (id),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);
