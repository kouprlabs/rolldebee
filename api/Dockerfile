FROM gradle:jdk17 as builder
WORKDIR /build
COPY . .
RUN gradle assemble

FROM gradle:jdk17
WORKDIR /app
COPY --from=builder /build/build/libs .
COPY --from=builder /build/src/main/resources/application.yml .

ENTRYPOINT ["java", "-jar", "./rolldebee-1.0.0.jar"]
EXPOSE 8080/tcp