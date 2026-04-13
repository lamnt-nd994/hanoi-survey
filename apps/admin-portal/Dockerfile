FROM node:20-alpine AS build
WORKDIR /app

ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

COPY package.json package-lock.json ./
COPY apps/web-client/package.json apps/web-client/package.json
COPY apps/admin-portal/package.json apps/admin-portal/package.json

RUN npm ci

COPY apps/admin-portal ./apps/admin-portal

RUN npm run build --workspace admin-portal

FROM nginx:1.27-alpine
COPY apps/admin-portal/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/apps/admin-portal/dist /usr/share/nginx/html

EXPOSE 80
