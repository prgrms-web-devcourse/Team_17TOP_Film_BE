CREATE TABLE users
(
    `id`                      bigint      NOT NULL AUTO_INCREMENT,
    `nickname`                varchar(20) NOT NULL,
    `provider`                varchar(20) NOT NULL,
    `provider_id`             varchar(80) NOT NULL,
    `profile_image`           varchar(255) NULL,
    `profile_thumbnail_image` varchar(255) NULL,
    `created_at`              TIMESTAMP   NOT NULL,
    `updated_at`              TIMESTAMP   NOT NULL,
    `is_deleted`              tinyint(1) NOT NULL,
    `deleted_at`              TIMESTAMP NULL,
    `last_login_at`           TIMESTAMP NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (id)
);


CREATE TABLE permissions
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_permission
(
    id            bigint NOT NULL,
    group_id      bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE auths
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    username      varchar(20) NOT NULL,
    provider      varchar(20) NOT NULL,
    provider_id   varchar(80) NOT NULL,
    profile_image varchar(255) DEFAULT NULL,
    group_id      bigint      NOT NULL,
    user_id       bigint       DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_username UNIQUE (username),
    CONSTRAINT unq_provider_and_id UNIQUE (provider, provider_id),
    CONSTRAINT fk_group_id_for_auths FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_user_id_for_auths FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- post_states Table Create SQL
CREATE TABLE post_states
(
    `id`    bigint      NOT NULL AUTO_INCREMENT,
    `post_state_value` varchar(20) NOT NULL,
    CONSTRAINT PK_POST_STATES PRIMARY KEY (id)
);


-- posts Table Create SQL
CREATE TABLE posts
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `author_id`    bigint       NOT NULL,
    `state_id`     bigint       NOT NULL,
    `title`        varchar(255) NOT NULL,
    `preview_text` varchar(255) NULL,
    `available_at` DATE NULL,
    `created_at`   TIMESTAMP    NOT NULL,
    `updated_at`   TIMESTAMP    NOT NULL,
    `is_deleted`   tinyint(1) NOT NULL,
    `deleted_at`   TIMESTAMP NULL,
    `latitude`     DOUBLE       NOT NULL,
    `longitude`    DOUBLE       NOT NULL,
    CONSTRAINT PK_POSTS PRIMARY KEY (id)
);

ALTER TABLE posts
    ADD CONSTRAINT FK_users_TO_posts_1 FOREIGN KEY (author_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE posts
    ADD CONSTRAINT FK_post_states_TO_posts_1 FOREIGN KEY (state_id)
        REFERENCES post_states (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- post_details Table Create SQL
CREATE TABLE post_details
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `post_id`   bigint NOT NULL,
    `opener_id` bigint NULL,
    `opened_at` DATE NULL,
    `content`   TEXT NULL,
    CONSTRAINT PK_POST_DETAILS PRIMARY KEY (id)
);

ALTER TABLE post_details
    ADD CONSTRAINT FK_posts_TO_post_details_1 FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE post_details
    ADD CONSTRAINT FK_users_TO_post_details_1 FOREIGN KEY (opener_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- post_images Table Create SQL
CREATE TABLE post_images
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `post_detail_id` bigint       NOT NULL,
    `original_url`   varchar(255) NOT NULL,
    `small_size_url` varchar(255) NULL,
    `image_order`    int          NOT NULL,
    CONSTRAINT PK_POST_IMAGES PRIMARY KEY (id)
);

ALTER TABLE post_images
    ADD CONSTRAINT FK_post_details_TO_post_images_1 FOREIGN KEY (post_detail_id)
        REFERENCES post_details (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- post_authorities Table Create SQL
CREATE TABLE post_authorities
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `post_id`   bigint NOT NULL,
    CONSTRAINT PK_AUTHORITIES PRIMARY KEY (id)
);

ALTER TABLE post_authorities
    ADD CONSTRAINT FK_post_authorities_user_id_users_id FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE post_authorities
    ADD CONSTRAINT FK_post_authorities_post_id_posts_id FOREIGN KEY (post_id)
        REFERENCES posts (id) ON DELETE RESTRICT ON UPDATE RESTRICT;
