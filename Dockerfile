# Stage 1: Build stage with Maven and Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and POM
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies for layer caching
RUN ./mvnw dependency:go-offline -B

# Copy source code and build JAR package
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Lightweight runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S qagroup && adduser -S qauser -G qagroup

# Copy compiled JAR artifact
COPY --from=builder /app/target/*.jar app.jar

# Create directories for local uploads & runtime data
RUN mkdir -p /app/uploads /app/data && chown -R qauser:qagroup /app

USER qauser

EXPOSE 8080

ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
