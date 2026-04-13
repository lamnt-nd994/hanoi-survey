# Hanoi Survey Backend

Spring Boot backend for public website and CMS admin.

## Stack

- Java 21
- Spring Boot 3.3
- Spring Data JPA
- Flyway
- MySQL 8

## Run locally

### 1) Start MySQL with Docker

```bash
docker compose up -d
```

Services after startup:

- MySQL: `localhost:3310` (default, configurable by `MYSQL_PORT`)
- Adminer (DB UI): `http://localhost:8081`

Adminer login:

- System: `MySQL`
- Server: `mysql` (if opened from container network) or `localhost`
- Username: `root`
- Password: `root`
- Database: `hanoi_survey`

### 2) Run Spring Boot API

```bash
mvn spring-boot:run
```

If Maven is not installed locally, run with Dockerized Maven wrapper:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

If you use environment variables:

```bash
set DB_URL=jdbc:mysql://localhost:3310/hanoi_survey?useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=Asia/Ho_Chi_Minh
set DB_USERNAME=root
set DB_PASSWORD=root
mvn spring-boot:run
```

Default DB config in `src/main/resources/application.yml`:

- URL: `jdbc:mysql://localhost:3310/hanoi_survey`
- Username: `root`
- Password: `root`

## API docs

- Swagger UI: `http://localhost:8080/swagger`

## Stop services

```bash
docker compose down
```

If you also want to remove DB data volume:

```bash
docker compose down -v
```

## Main endpoint groups

- Public: `/api/public/v1/*`
- Admin: `/api/admin/v1/*`
