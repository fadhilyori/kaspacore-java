FROM amazoncorretto:17-alpine3.20-jdk

WORKDIR /app

# Install maven
RUN apk add --no-cache maven

COPY pom.xml .

RUN mvn dependency:resolve -Dmaven.test.skip=true

COPY . .

RUN mvn package -Dmaven.test.skip=true
