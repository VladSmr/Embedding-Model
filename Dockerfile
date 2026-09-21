FROM eclipse-temurin:21-jdk-alpine
# Что делает: берёт готовый базовый образ как основу.
# eclipse-temurin:17-jdk-alpine — это Java 17 JDK в минимальном образе Alpine Linux.
# Alpine — это урезанный Linux, весит ~5 МБ. B отличие от обычного Ubuntu (~80 МБ) — маленький и быстрый.
WORKDIR /app
# Что делает: создаёт папку /app внутри образа и делает её текущей.
COPY target/Embedding-Model-0.0.1-SNAPSHOT.jar app.jar
# Что делает: копирует target/myapp.jar с твоего компьютера в /app/app.jar внутри образа.
ENTRYPOINT ["java", "-jar", "app.jar"]
# Что делает: команда, которая выполнится при запуске контейнера.