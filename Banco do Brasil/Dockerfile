# Define a imagem base
FROM adoptopenjdk:11-jdk-hotspot

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR para o contêiner
COPY target/Banco-do-Brasil-0.0.1-SNAPSHOT.jar /app

# Define a porta exposta pelo contêiner
EXPOSE 8017

# Comando de inicialização do aplicativo
CMD ["java", "-jar", "Banco-do-Brasil-0.0.1-SNAPSHOT.jar"]