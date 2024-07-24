FROM openjdk:20-jdk-slim-buster
WORKDIR /app
COPY "out/artifacts/n_news_service_jar/n-news-service.jar" /app/n-user-service.jar
COPY "src/main/resources/application.yml" /app/application.yml
ENTRYPOINT ["java","-jar","n-user-service.jar", "--spring.config.location=file:/app/application.yml"]