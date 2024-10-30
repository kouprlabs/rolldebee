FROM gradle:jdk21 as build-env
WORKDIR /build
COPY . .
RUN gradle assemble

FROM gradle:jdk21
WORKDIR /app
COPY --from=build-env /build/build/libs .
COPY --from=build-env /build/src/main/resources/application.yml .

ENTRYPOINT ["java", "-jar", "./sqlgen-1.0.0.jar"]
EXPOSE 9898/tcp