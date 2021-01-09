FROM maven:3.6.1-jdk-13-alpine AS MAVEN_BUILD

COPY ./ ./

RUN mvn clean package -Dmaven.test.skip=true


FROM openjdk:13-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY --from=MAVEN_BUILD ${JAR_FILE} app.jar

CMD ["java","-jar","/app.jar"]