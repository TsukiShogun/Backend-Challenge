FROM openjdk:21-jdk-slim AS build
WORKDIR /app

COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY gradlew /app/gradlew
COPY src /app/src

RUN chmod +x /app/gradlew
RUN ./gradlew build --no-daemon

FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080