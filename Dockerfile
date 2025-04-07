# Etapa de compilación
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties* ./
COPY gradle ./gradle
COPY src ./src

RUN rm -rf /home/gradle/.gradle/caches/modules-2/files-2.1/io.grpc/protoc-gen-grpc-java

RUN gradle build --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/grpc-challenge-lafuente-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
