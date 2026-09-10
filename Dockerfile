FROM node:22-alpine AS frontend-build
WORKDIR /app/frontend

COPY frontend/package*.json ./
RUN npm install

COPY frontend/ ./
RUN npm run build

FROM eclipse-temurin:25-jdk AS backend-build
WORKDIR /app/backend

COPY backend/.mvn .mvn
COPY backend/mvnw backend/pom.xml ./
COPY backend/src src
COPY --from=frontend-build /app/frontend/dist/ src/main/resources/static/

RUN chmod +x mvnw
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=backend-build /app/backend/target/*.jar app.jar

ENV PORT=10000
EXPOSE 10000

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
