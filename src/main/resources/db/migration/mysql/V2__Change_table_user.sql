ALTER TABLE user_roles DROP FOREIGN KEY fk_user_roles_user;

ALTER TABLE user RENAME TO app_user;

ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE;