# Use official Java 17 image
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy all project files
COPY . .

# Give permission to mvnw
RUN chmod +x mvnw

# Build the jar
RUN ./mvnw package -DskipTests

# Expose port (optional but good)
EXPOSE 8080

# Run the generated jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]