# syntax=docker/dockerfile:1
# Stage 1 — Build Angular
FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY frontend/package*.json ./
RUN --mount=type=cache,target=/root/.npm \
    npm install
COPY frontend/ ./
RUN npm run build

# Stage 2 — Build Spring Boot WAR
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /backend
COPY backend/pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -q
COPY backend/src ./src
COPY --from=frontend-build /frontend/dist/mandiri-cctv-dashboard/browser /frontend/dist/mandiri-cctv-dashboard/browser
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -Pskip-frontend -q

# Stage 3 — Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /backend/target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
