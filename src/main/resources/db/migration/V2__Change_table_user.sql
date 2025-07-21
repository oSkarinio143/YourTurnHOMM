ALTER TABLE `user` RENAME TO `app_user`;

-- Krok 2: Zmiana nazwy tabeli 'user_roles' na 'app_user_roles' dla zachowania spójności.
ALTER TABLE `user_roles` RENAME TO `app_user_roles`;

-- Krok 3: Naprawa klucza obcego w tabeli 'app_user_roles'.
-- Najpierw musimy usunąć stary klucz obcy, który wciąż odnosi się do nieistniejącej już nazwy 'user'.
-- Nazwa ograniczenia to 'fk_user_roles_user', jak zdefiniowano w V1.
ALTER TABLE `app_user_roles` DROP FOREIGN KEY `fk_user_roles_user`;

-- Krok 4: Dodanie nowego, poprawnego klucza obcego, który wskazuje już na tabelę 'app_user'.
ALTER TABLE `app_user_roles` ADD CONSTRAINT `fk_app_user_roles_app_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `app_user`(`id`)
    ON DELETE CASCADE;

-- Krok 5 (Dobra praktyka): Zmiana nazwy ograniczenia w tabeli 'app_user' dla spójności.
-- Stara nazwa ograniczenia to 'fk_user_refresh_token'.
ALTER TABLE `app_user` RENAME INDEX `fk_user_refresh_token` TO `fk_app_user_refresh_token`;