FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/soa-spacemarine-0.0.1.jar /app/app.jar
EXPOSE 7008
CMD ["java", "-jar", "app.jar"]