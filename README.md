
# YourTurnHOMM

Symulator pojedynków bazujący na mechanikach i jednostkach z gry **Heroes of Might & Magic 3**. 

> 🎥 Zalecam spojrzeć na filmiki, szybkie przejście przez wszystkie elementy z krótkim opisem i podkreśleniem najważniejszych
- 💻 **Prezentacja projektu (2 minuty):** [Oglądaj tutaj](https://www.youtube.com/watch?v=xfvLEBHoVVo)  
- 🛠️ **Prezentacja kodu (6 minut):** [Oglądaj tutaj](https://www.youtube.com/watch?v=2GtfnAiVhPM)


## 🚀 Technologie
- **Język:** Java  
- **Frameworki:** Spring Boot, Thymeleaf  
- **Bazy danych:** H2 (gotowy do uruchomienia), MySQL (wymaga konfiguracji)  
- **Bezpieczeństwo:** JWT, odświeżanie tokenów, role, BearerTokenAuthenticationFilter, szyfrowanie haseł, XSS filter, CSP nonce filter, SameSite w cookies, FrameOptions, zapytania sparametryzowane  
- **Architektura:** DDD (Domain-Driven Design), REST API  
- **Inne mechanizmy w Javie:** wielowątkowość, SseEmitter, niestandardowe filtry, własne adnotacje, walidacja, obsługa błędów
- **Logowanie:** Docker, ElasticSearch, Kibana, traceId w każdym logu

## ⚡ Uruchomienie
### Profil Mysql (wymaga konfiguracji)
### Profil h2 (polecany)
Projekt gotowy do uruchomienia bez dodatkowej konfiguracji:  
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
