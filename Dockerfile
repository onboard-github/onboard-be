FROM node:latest AS NODE_BUILDER

RUN mkdir /app_source
COPY . /app_source

WORKDIR /app_source/adapter-in/admin/frontend

RUN npm install
RUN npm run build

FROM comforest/bol_build_image:4 AS BUILDER

COPY --from=NODE_BUILDER /app_source /app_source
WORKDIR /app_source

RUN chmod +x ./gradlew
RUN ./gradlew generateRedoc
RUN ./gradlew :adapter-in:web:bootJar

FROM eclipse-temurin AS RUNNER

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
