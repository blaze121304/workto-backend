# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -q
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -q

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
#COPY src/main/resources/key/keyfile.properties /app/keyfile.properties  #KEY파일 복사
EXPOSE 10008
ENTRYPOINT ["java", "-jar", "app.jar"]