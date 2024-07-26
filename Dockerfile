FROM openjdk:20
WORKDIR /app
COPY "target/n-news-service-0.0.1-SNAPSHOT.jar" /app/n-news-service.jar
COPY "src/main/resources/application.yml" /app/application.yml
ENTRYPOINT ["java","-jar","n-news-service.jar", "--spring.config.location=file:/app/application.yml"]