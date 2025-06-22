FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/cloudbasedproject.jar app.jar

ENV AWS_S3_BUCKET_NAME=cloudbasedproject-bucket
ENV AWS_REGION=eu-west-1

EXPOSE 2020

ENTRYPOINT [ "sh", "-c", "echo SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL && java -jar app.jar" ]

