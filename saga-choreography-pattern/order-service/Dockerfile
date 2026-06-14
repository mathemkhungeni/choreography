#FROM maven:3.8-eclipse-temurin-8 AS build
#WORKDIR /app
#
#COPY pom.xml .
#COPY common-dtos/pom.xml common-dtos/
#COPY order-service/pom.xml order-service/
#COPY payment-service/pom.xml payment-service/
#
#RUN mvn -B dependency:resolve -pl order-service -am -DskipTests
#
#COPY common-dtos/src common-dtos/src
#COPY order-service/src order-service/src
#
#RUN mvn -B package -pl order-service -am -DskipTests
#
#FROM eclipse-temurin:8-jre
#WORKDIR /app
#
#COPY --from=build /app/order-service/target/order-service-*.jar app.jar
#
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.8-eclipse-temurin-8 AS build
WORKDIR /app

COPY pom.xml .
COPY common-dtos/pom.xml common-dtos/
COPY order-service/pom.xml order-service/
COPY payment-service/pom.xml payment-service/

RUN mvn -B install -N -DskipTests

COPY common-dtos/src common-dtos/src
RUN mvn -B install -pl common-dtos -DskipTests

COPY order-service/src order-service/src
RUN mvn -B package -pl order-service -DskipTests

FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/order-service/target/order-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]