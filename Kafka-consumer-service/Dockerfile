# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

EXPOSE 8084
# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/Kafka-consumer-service-0.0.1-SNAPSHOT.jar /app/consumer-service.jar

# Define the command to run your application
CMD ["java", "-jar", "consumer-service.jar"]
