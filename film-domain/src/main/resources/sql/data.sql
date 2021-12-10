INSERT INTO permissions(id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN')
;

INSERT INTO groups(id, name)
VALUES (1, 'USER_GROUP'),
       (2, 'ADMIN_GROUP')
;

-- USER_GROUP (ROLE_USER)
-- ADMIN_GROUP (ROLE_USER, ROLE_ADMIN)
INSERT INTO group_permission(id, group_id, permission_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 2, 2)
;

-- post_state
INSERT INTO post_states
VALUES (0, 'CLOSED'),
       (1, 'OPENALBE'),
       (2, 'OPENED')
;

INSERT INTO users(nickname, provider, provider_id, created_at, updated_at, is_deleted)
VALUES ('iyj', 'kakao', '2009852607', '2021-12-10 15:12:34.060692', '2021-12-10 15:12:34.060692', 0)
;


INSERT INTO auths(username, provider, provider_id, group_id, user_id)
VALUES ('임연재', 'kakao', '2009852607', 1, 1)
;

