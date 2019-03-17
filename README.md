"# todolistakkahttp"
Build docker image with sbt
sbt docker:publishLocal

FROM openjdk:jre-alpine
WORKDIR /opt/docker
ADD opt /opt
RUN ["chown", "-R", "daemon:daemon", "."]
USER daemon
ENTRYPOINT ["bin/todolistakkahttp"]
CMD []

todolistapplication/docker-compose.yml
reactclient akkahttpserver
