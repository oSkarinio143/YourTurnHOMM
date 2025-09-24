PREZENTACJA PROJEKTU (2 MINUTY) - https://www.youtube.com/watch?v=xfvLEBHoVVo

PREZENTACJA KODU (6 MINUT) - https://www.youtube.com/watch?v=2GtfnAiVhPM


YourTurnHOMM - Projekt do symulowania pojedynków wykorzystujący mechaniki oraz jednostki z gry Heroes of might & magic 3.

Projekt oparty o Java + Spring + Thymeleaf. Backend pisałem sam, widoki w większości wygenerowane przez AI z moimi drobnymi poprawkami.

W projekcie są dwa profile jeden - Mysql wymaga konfiguracji połączenie z bazą + danych administratora, w drugim - h2 wszystko jest widoczne i skonfigurowane. Zachęcam do uruchomienia projektu z profilu h2 - mvn spring-boot:run -Dspring-boot.run.profiles=h2

Użytkownik administracyjny - adminUser

Hasło - 1234

Elementy projektu: 
REST API, architektura DDD, Jwt, role, odświeżanie tokenów, cookies,  BearerTokenAuthenticationFilter, wielowątkowość, SseEmitter, Spring Profiles, niestandardowe filtry, logowanie (docker, elasticsearch, kibana), wykorzystanie mdc, walidacja, własne adnotacje, obsługa błędów, czytelne wyświetlanie wiadomości błędów (modale)

Baza danych: 
relacje, seedowanie danych, automatyczne tworzenie pierwszego admina, obsługa dwóch rodzajów baz danych (h2 i mysql), GitHub Pages do ładowania obrazków

Ustawienia bezpieczeństwa: Filter XSS, Mechanizm cspnonce ogranicza skąd ładować zasoby, SameSite ustawione w ciasteczkach, FrameOptions, szyfrowanie haseł, używam zapytań sparametryzowanych,
