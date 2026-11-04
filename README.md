# Сервер метеостанции

Серверная часть проекта метеостанции на ESP32 и DHT11. Принимает измерения по HTTP,
хранит их в PostgreSQL и отображает на веб-странице.

## Стек

- Backend: Java, Maven, Spring Boot 3.5 (web, data-jpa, thymeleaf, validation, actuator)
- DB: PostgreSQL
- Frontend: Thymeleaf, Chart.js

## Структура

```
src/main/java/              # код приложения
src/main/resources/         # application.properties, templates/, static/
```
