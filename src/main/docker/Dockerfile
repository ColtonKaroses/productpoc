FROM java:8
ADD productpoc-latest.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:postgresql://db:5432/postgres","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]