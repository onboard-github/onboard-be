FROM eclipse-temurin:17-jdk-alpine AS BUILDER

RUN mkdir /app_source
COPY . /app_source

WORKDIR /app_source

RUN chmod +x ./gradlew
RUN ./gradlew :adapter-in:web:copySwaggerUI
RUN ./gradlew :adapter-in:web:bootJar

FROM eclipse-temurin:17-jdk-alpine AS RUNNER

RUN mkdir /app

COPY --from=BUILDER /app_source/adapter-in/web/build/libs /app

WORKDIR /app

ENV TZ=Asia/Seoul

EXPOSE 8080
USER nobody

ARG PHASE

ENV ENV_PHASE=${PHASE}

ENTRYPOINT java -jar \
  -Dspring.profiles.active=${ENV_PHASE:-sandbox} \
  /app/*.jar
