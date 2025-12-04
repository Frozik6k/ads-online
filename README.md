# Ads Online

Приложение на Spring Boot 3 для размещения и управления объявлениями с поддержкой базовой-аутентификации, загрузки изображений и документирования API через Swagger/OpenAPI.

## Требования
- JDK 17
- Maven 3.9+
- Docker и Docker Compose (для запуска в контейнерах)

## Конфигурация
Основные параметры настраиваются через переменные окружения (см. `application.properties`), в том числе:
- `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` — подключение к PostgreSQL.
- `UPLOAD_PATH` — директория для сохранения загружаемых файлов (по умолчанию `./uploads`).
- `security.jwt.secret`, `security.token.lifetime` — параметры JWT-токенов.

## Запуск локально
1. Убедитесь, что PostgreSQL доступен по параметрам, указанным выше.
2. Соберите и запустите приложение:
   ```bash
   ./mvnw spring-boot:run
   ```
3. После старта API будет доступно на `http://localhost:8080`, Swagger UI — на `/swagger-ui.html`.

## Запуск через Docker Compose
1. Соберите контейнер и поднимите сервисы (приложение и PostgreSQL):
   ```bash
   docker-compose up --build
   ```
2. Папка `./uploads` на хосте монтируется внутрь контейнера для сохранения файлов.

## Взаимодействие с фронтендом
Готовый фронтенд для проверки можно запустить отдельным контейнером:
```bash
docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21
```
Он ожидает бекенд на `http://localhost:8080`.

## Дополнительно
- Готовая спецификация API находится в файле `api-docs.yaml` в корне проекта.
- Liquibase-миграции расположены в `src/main/resources/liquibase/` и применяются при запуске.
