FROM maven:4.0.0-rc-4-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests=true

FROM tomcat:10.1.48-jre21-temurin-noble
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080