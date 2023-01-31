FROM docker.io/openjdk:17-slim-buster AS openjdk-gradle

ENV GRADLE_HOME /opt/gradle

RUN set -o errexit -o nounset \
    && echo "Adding gradle user and group" \
    && groupadd --system --gid 1000 gradle \
    && useradd --system --gid gradle --uid 1000 --shell /bin/bash --create-home gradle \
    && mkdir /home/gradle/.gradle \
    && chown --recursive gradle:gradle /home/gradle \
    \
    && echo "Symlinking root Gradle cache to gradle Gradle cache" \
    && ln -s /home/gradle/.gradle /root/.gradle

VOLUME /home/gradle/.gradle

WORKDIR /opt

RUN apt-get update && apt-get upgrade --yes
FROM docker.io/openpolicyagent/opa:0.46.1-static as opa-env
FROM openjdk-gradle AS walt-build
COPY ./ /opt
RUN chmod +x gradle
RUN ./gradlew clean build
RUN tar xf /opt/build/distributions/waltid-nftkit-*.tar -C /opt

FROM openjdk:17-slim-buster

COPY --from=opa-env /opa /usr/local/bin/opa

RUN mkdir /app
COPY --from=walt-build /opt/waltid-nftkit-* /app/



WORKDIR /app

ENTRYPOINT ["/app/bin/waltid-nftkit"]
