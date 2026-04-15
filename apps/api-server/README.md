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

## Production deploy with Docker / Dokploy

Files added for production:

- `Dockerfile`: build and run Spring Boot in image `eclipse-temurin:21-jre-jammy`
- `docker-compose.prod.yml`: production stack for API + MySQL (+ Adminer optional)
- `.env.prod.example`: sample environment variables for Dokploy/server
- `deploy/dokploy/*`: split Dokploy deployment files for `mysql`, `api-server`, `web-client`, `admin-portal`

## UAT deploy with Docker Compose

Files added for UAT:

- `src/main/resources/application-uat.yml`: Spring Boot `uat` profile
- `docker-compose.uat.yml`: UAT stack for API + MySQL (+ Adminer optional)
- `.env.uat.example`: sample UAT environment variables

Prepare host folders first, then copy `.env.uat.example` to your UAT environment and replace at least:

- `JWT_SECRET`
- `APP_CORS_ALLOWED_ORIGINS`
- `HOST_UPLOADS_DIR`
- `HOST_MYSQL_DATA_DIR`
- `MYSQL_PASSWORD`
- `MYSQL_ROOT_PASSWORD`

Run UAT from `apps/api-server`:

```bash
docker compose --env-file .env.uat -f docker-compose.uat.yml up -d --build
```

Default UAT ports:

- API: `http://localhost:8082`
- MySQL: `localhost:3311`
- Adminer with tools profile: `http://localhost:8083`

To include Adminer:

```bash
docker compose --env-file .env.uat -f docker-compose.uat.yml --profile tools up -d --build
```

`application-uat.yml` keeps Swagger enabled by default for testing. Set `SPRINGDOC_ENABLED=false` in `.env.uat` if UAT should behave like production.

### 1) Prepare host folders on server

Create 2 absolute directories on your server before deploy:

- one for uploaded files, for example: `/srv/hanoi-survey/uploads`
- one for MySQL data, for example: `/srv/hanoi-survey/mysql`

This is important because the production compose file uses bind mounts:

- `HOST_UPLOADS_DIR -> /var/opt/hanoi-survey/uploads`
- `HOST_MYSQL_DATA_DIR -> /var/lib/mysql`

With this setup, files uploaded from the CMS will exist directly on your server folder, so you can inspect them outside the container.

### 2) Configure environment variables

Copy `.env.prod.example` to your production environment and replace at least these values:

- `JWT_SECRET`
- `APP_CORS_ALLOWED_ORIGINS`
- `HOST_UPLOADS_DIR`
- `HOST_MYSQL_DATA_DIR`
- `MYSQL_PASSWORD`
- `MYSQL_ROOT_PASSWORD`

### 3) Deploy in Dokploy

Recommended Dokploy setup is the split deployment in `deploy/dokploy/`:

- `deploy/dokploy/mysql.compose.yml`
- `deploy/dokploy/api.compose.yml`
- `deploy/dokploy/web.compose.yml`
- `deploy/dokploy/admin.compose.yml`

Use `deploy/dokploy/README.md` for the deployment order and env files.

The old all-in-one compose is still available if you want one-shot deployment:

If you deploy this backend via Docker Compose in Dokploy, use:

- compose file: `apps/api-server/docker-compose.prod.yml`
- context / working directory: `apps/api-server`

Recommended production domains:

- public/admin frontend domains must be included in `APP_CORS_ALLOWED_ORIGINS`
- uploaded files will be public under `https://your-api-domain/uploads/...`

### 4) Production application profile

`application-prod.yml` is configured for container deployment:

- default MySQL host inside Docker network: `mysql:3306`
- upload directory inside container: `/var/opt/hanoi-survey/uploads`
- proxy-aware headers enabled with `server.forward-headers-strategy: framework`
- Swagger disabled in production

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
