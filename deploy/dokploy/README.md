# Dokploy Deployment

Recommended production setup in Dokploy is to deploy each part separately:

1. `mysql`
2. `api-server`
3. `web-client`
4. `admin-portal`

This gives you cleaner logs, safer restarts, and easier rollbacks.

## Server folders

Prepare these folders on the server first:

- `/srv/hanoi-survey/uploads`
- `/srv/hanoi-survey/mysql`

Uploaded files from the backend will be stored directly in `/srv/hanoi-survey/uploads`.

## Suggested domains

- Public site: `https://khaosathanoi.com.vn`
- Admin site: `https://admin.khaosathanoi.com.vn`
- API: `https://api.khaosathanoi.com.vn`

## Dokploy apps

### 1) MySQL

- Compose file: `deploy/dokploy/mysql.compose.yml`
- Env file: `deploy/dokploy/mysql.env.example`

### 2) API server

- Compose file: `deploy/dokploy/api.compose.yml`
- Env file: `deploy/dokploy/api.env.example`

Important:

- `DB_URL` must point to the MySQL app you deployed in Dokploy
- `HOST_UPLOADS_DIR` must match your real server folder
- `api-server`, `web-client`, and `admin-portal` do not need host port publishing in Dokploy; routing is handled by domains and container ports

### 3) Web client

- Compose file: `deploy/dokploy/web.compose.yml`
- Env file: `deploy/dokploy/web.env.example`

### 4) Admin portal

- Compose file: `deploy/dokploy/admin.compose.yml`
- Env file: `deploy/dokploy/admin.env.example`

## Deploy order

1. Deploy MySQL
2. Deploy API and confirm it connects to DB
3. Deploy web-client
4. Deploy admin-portal

## Notes

- The repository root `docker-compose.prod.yml` is still useful for all-in-one deploys or quick staging.
- For real Dokploy production, prefer the split files in this folder.
