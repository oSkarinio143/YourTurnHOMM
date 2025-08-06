YourTurnHOMM - Projekt do symulowania pojedynków wykorzystujący mechaniki oraz jednostki z gry Heroes of might & magic 3.

Projekt oparty o Java + Spring + Thymeleaf. Backend pisałem sam, widoki w większości wygenerowane przez AI z moimi drobnymi poprawkami.

Uruchamianie:
1. w cmd przechodzę do wybranego folderu
2. git clone https://github.com/oSkarinio143/YourTurnHOMM.git
3. cd YourTurnHOMM
4. mvn spring-boot:run -Dspring-boot.run.profiles=h2

Użytkownik - adminUser

Hasło - 1234


Funckje
1. Baza danych:
    - Dodawanie jednostek
    - Wyświetlanie jednostek
    - Modyfikowanie jednostek
    - Usuwanie jednostek
2. Tryb pojedynyku:
    - Wybór jednostek, ilości oraz statystyk
    - Tryb walki, pojedynek toczony jest automatycznie
3. Panel admina (Widoczny tylko dla adminów):
    - Wyświetlanie użytkowników
    - Nadawanie uprawnień administracyjnych
    - Usuwanie użytkowników (Brak możliwości usunięcia samego siebie i admina głównego

Techniczne:
1. Baza danych:
    - Seedowanie bazy danych: Przy pierwszym uruchomieniu do bazy danych dodane zostaną jednostki frakcji zamku
    - Pierwszy admin: Przy pierwszym uruchomieniu automatycznie zostanie utworzony pierwszy użytkownik który jest adminem
    - Dwa profile: Projekt można odpalić na dwóch profilach.
        - MySQL: Profil MySql, główny, zawiera ukryte w zmiennych środowiskowych nazwę, hasło i klucz sekretny
        - H2: Profil H2, szybkie uruchamianie, wszystko ustawione w [application.properties](http://application.properties) widoczne dla każdego użytkownika
    - Wykorzystanie GitHub Pages do wyświetalnia grafik jednostek
2. Pojedynek:
    - Dane wybrane przez użytkownika między zadaniami są cały czas przekazywane w obiekcie aby były dostępne
    - Logika pojedynku:
        - Atak: jeden punkt więcej od przeciwnika zwiększa obrażenia o 5% (maks 300%)
        - Obrona: jeden punkt więcej od przeciwnika zmniejsza otrzymywane obrażenia o 2.5% (maks 70%)
        - Szybkość: szybsza jednostka atakuje pierwsza. Równa szybkość = losowanie
        - Strzały: jednostki dystansowe działają tak samo jak walczące w zwarciu. Tak naprawdę nic to nie zmienia
        - Opis: umiejętności specjalne nie mają żadnego działania.
    - Pojedynek toczy się tworząc kolejkę (wiadomości) które wyświetlane są w odstępach czasowych 
    - Wielowątkowość: pojedynek idzie dalej i nie musi czekać na wyświetlenie wiadomości
    - Każdy pojedynek ma swój unikalny SSEmitter do streamowania wiadomości.
3. Sieciowe:
    - Stateless JWT:
        - AccessToken: Data przydatności 15m
        - RefreshToken: Data przydatności 7d, gdy nie wygaśnięty użytkownik cały czas jest zalogowany, po wygaśnięciu trzeba się zalogować.
        - RefreshFilter: Odświeża refresh i access tokeny. Oryginalny mechanizm odświeżania wykorzystuje mocno frontend więc zdecydowałem się na taki prosty zamiennik
        - BearerTokenAuthenticationFilter: Główny filter do uwierzytelniania użykowników.
    - Cookies: Ciasteczka przechowują tokeny.
    - Role: Rola użytkownika ogranicza nieporządane działania.
    - Automatyczne przekierowania: Kiedy użytkownik wchodzi na endpoint do którego nie ma uprawnień, nie jest zalogowany dostaje automatyczne przekierowanie.
4. Walidacja:
    - BindingResult: Proste błędy @Valid wyłapywane w kontrolerze
    - ExceptionHandler: Błędy związane z logiką biznesową
    - Własne adnotacje: Dla specyficznych błędów
    - Wyświetlanie modali: Każdy błąd, skutkuje wysłaniem odpowiedniej wiadomości na front i wyświetleniem wiadomości w okienku modal.
    - Tworzenie Wiadomości: Wykorzystanie wzorów w message.properties.
5. Bezpieczeństwo:
    - SQLi: Używam zapytań parametryzowanych
    - Csrf: Ciasteczka mają ustawione SameSite
    - XSS: Specjalny wrapper i filter uniemożliwiające wykonanie jakiegoś nieporządanego kodu oraz httpOnly w ciasteczkach uniemożliwiający dostęp do nich przez js.
    - CSP: Mechanizm nonce ogranicza z jakich źródeł można ładować zasoby
    - HTTPS: Secure w ciasteczkach, oraz hsts w konfiguracji pozwala na komunikację jedynie przez https
    - FrameOptions: Całkowicie blokuje możliwość osadzania Twojej strony w ramkach
    - ContentTypeOption: Pozwala na ładowanie jedynie plików których typ jest zgodny z nagłówkiem
    - Szyfrowanie haseł
  
