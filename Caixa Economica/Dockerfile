# Define a imagem base
FROM adoptopenjdk:11-jdk-hotspot

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR para o contêiner
COPY target/Caixa-Economica-0.0.1-SNAPSHOT.jar /app

# Define a porta exposta pelo contêiner
EXPOSE 8018

# Comando de inicialização do aplicativo
CMD ["java", "-jar", "Caixa-Economica-0.0.1-SNAPSHOT.jar"]