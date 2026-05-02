FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/DesafioUnMEP-1.0-SNAPSHOT.jar app.jar
COPY data.json .
CMD ["java", "-jar", "app.jar"]