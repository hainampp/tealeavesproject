FROM maven:3.9.9-eclipse-temurin-23-alpine as build

COPY . /opt/

WORKDIR /opt

RUN mvn install -DskipTests && mvn clean package -DskipTests

FROM maven:3.9.9-eclipse-temurin-23-alpine
COPY --from=build opt/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]