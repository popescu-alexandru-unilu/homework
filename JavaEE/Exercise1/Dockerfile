# ===== Stage 1: build the WAR =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ===== Stage 2: WildFly with MariaDB driver =====
FROM quay.io/wildfly/wildfly:latest-jdk17

USER root

# Keep this in sync with pom.xml <mariadb.version>
ARG MARIADB_VERSION=3.5.6

# MariaDB module dir
RUN mkdir -p /opt/jboss/wildfly/modules/system/layers/base/org/mariadb/jdbc/main

# Copy the versioned driver JAR from build cache
COPY --from=build /root/.m2/repository/org/mariadb/jdbc/mariadb-java-client/${MARIADB_VERSION}/mariadb-java-client-${MARIADB_VERSION}.jar \
  /opt/jboss/wildfly/modules/system/layers/base/org/mariadb/jdbc/main/mariadb-java-client.jar

# Module descriptor
COPY module.xml /opt/jboss/wildfly/modules/system/layers/base/org/mariadb/jdbc/main/module.xml

# Deploy the application with context root "Exercise1"
COPY --from=build /app/target/Exercise1.war /opt/jboss/wildfly/standalone/deployments/Exercise1.war

USER jboss
EXPOSE 8080
