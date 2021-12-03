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