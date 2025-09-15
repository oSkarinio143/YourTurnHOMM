ALTER TABLE user_roles DROP FOREIGN KEY fk_user_roles_user;

-- Krok 2: Zmiana nazwy tabeli 'user' na 'app_user'.
ALTER TABLE user RENAME TO app_user;

-- Krok 3: Ponowne dodanie klucza obcego do tabeli 'user_roles',
-- tym razem wskazującego na nową nazwę tabeli 'app_user'.
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE;