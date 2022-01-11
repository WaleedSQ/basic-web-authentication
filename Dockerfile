# build step
FROM maven:3.5.2-jdk-8-alpine AS builder

RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /build/src
RUN mvn package -DskipTests

# package image step #
FROM openjdk:8 AS runtime
EXPOSE 8080

# set app home folder
ENV APP_HOME /app

RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME
COPY --from=builder /build/target/user-auth-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
