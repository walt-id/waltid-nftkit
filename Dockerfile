### Configuration

# set --build-args SKIP_TESTS=true to use
ARG SKIP_TESTS

# --- dos2unix-env    # convert line endings from Windows machines
FROM docker.io/rkimf1/dos2unix@sha256:60f78cd8bf42641afdeae3f947190f98ae293994c0443741a2b3f3034998a6ed as dos2unix-env
WORKDIR /convert
COPY gradlew .
RUN dos2unix ./gradlew

# --- build-env       # build the NFT Kit
FROM docker.io/gradle:7.5-jdk as build-env

ARG SKIP_TESTS

WORKDIR /appbuild

COPY . /appbuild

# copy converted Windows line endings files
COPY --from=dos2unix-env /convert/gradlew .

# cache Gradle dependencies
VOLUME /home/gradle/.gradle

RUN if [ -z "$SKIP_TESTS" ]; \
    then echo "* Running full build" && gradle -i clean build installDist; \
    else echo "* Building but skipping tests" && gradle -i clean installDist -x test; \
    fi

# --- opa-env
FROM docker.io/openpolicyagent/opa:0.50.2-static as opa-env


# --- app-env
FROM openjdk:17-slim-buster AS app-env

WORKDIR /app

COPY --from=opa-env /opa /usr/local/bin/opa

RUN ldconfig

COPY --from=build-env /appbuild/build/install/waltid-nftkit /app/


### Execution
EXPOSE 7000

ENTRYPOINT ["/app/bin/waltid-nftkit"]
