# Use an official OpenJDK runtime as a parent image
FROM maven:3.8-jdk-11

RUN mkdir /servidor
COPY . /servidor
WORKDIR /servidor
RUN mvn clean package

CMD ["java", "-jar", "target/servidor-1.0-SNAPSHOT-jar-with-dependencies.jar"]