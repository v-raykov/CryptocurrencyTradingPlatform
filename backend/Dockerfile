FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "crypto-platform-db:3306", "--", "java", "-jar", "app.jar"]
