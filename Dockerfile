FROM openjdk:8-jre-slim-bullseye

RUN DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y

WORKDIR /app

ADD target/*.jar /app

EXPOSE 8080

ENV JAVA_OPTS="-XshowSettings:vm"

CMD java $JAVA_OPTS -Dspring.profiles.active=kubernetes -jar api-connector-0.0.1-SNAPSHOT.jar
