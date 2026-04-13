# Hanoi Survey Repo Notes

## Workspace
- Root `package.json` is only for frontend workspaces. Backend is **not** an npm workspace; it lives in `apps/api-server` and uses Maven.
- Frontend apps are `apps/web-client` and `apps/admin-portal`.
- Frontend stack is **Vue 3 + Vite + vue-router + Tailwind**, not React.

## Frontend Commands
- Install frontend deps from repo root: `npm install`
- Run public site: `npm run dev:web`
- Run admin portal: `npm run dev:admin`
- Build both frontend apps: `npm run build`
- Build one app only: `npm run build:web` or `npm run build:admin`
- Vite ports are fixed in config: `web-client=5173`, `admin-portal=5174`.
- Both frontend apps default to `VITE_API_BASE_URL=http://localhost:8080`.

## Backend Commands
- Start MySQL/Adminer from `apps/api-server`: `docker compose up -d`
- MySQL host port is `3310`; Adminer is `8081`.
- Run backend from `apps/api-server` with local Maven: `mvn spring-boot:run`
- If Maven is missing, use the Dockerized wrapper: `./mvnw spring-boot:run` or PowerShell `./mvnw.cmd spring-boot:run`
- Build backend from `apps/api-server`: `./mvnw.cmd -DskipTests package`
- Swagger is at `http://localhost:8080/swagger`

## Backend Architecture
- Current Java package layout is layer-first:
  - `domain/*` = domain models and shared enums/exceptions only
  - `application/*` = services, commands, repository ports
  - `infrastructure/*` = Spring/JPA/config/security/adapters
  - `interfaces/rest/*` = controllers and request/response DTOs
- `BaseEntity` is in `infrastructure/persistence/shared`, not in `domain`.
- `application` should not import REST DTOs or JPA entities.
- `interfaces/rest` should not map directly from persistence entities; map from domain models.

## Backend Auth / API
- Auth endpoint: `POST /api/auth/v1/login`
- Public APIs are under `/api/public/v1/*`
- Admin APIs are under `/api/admin/v1/*` and require JWT roles.
- Security config currently permits `/api/auth/v1/login`, `/swagger/**`, `/v3/api-docs/**`, and `/api/public/**` without auth.
- Seeded admin account is created at startup if missing:
  - username: `admin`
  - password: `Admin@123`
- Admin frontend stores JWT in localStorage key: `hanoi-survey-admin-token`

## Data / Infra Gotchas
- Backend JPA uses `ddl-auto: validate`; schema changes must go through Flyway migrations in `apps/api-server/src/main/resources/db/migration/`.
- Default backend DB URL is `jdbc:mysql://localhost:3310/hanoi_survey...`; do not assume MySQL is on `3306`.
- JWT config comes from `app.jwt.*` in `application.yml`; local default secret is already present for dev.

## Verification
- There is no verified frontend test/lint pipeline yet; the main frontend verification is `npm run build`.
- There is no repo CI or extra instruction file checked into the repository right now; rely on source config over earlier prose.
