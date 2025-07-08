FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# Adiciona permissão de execução no Linux (Render)
RUN chmod +x ./mvnw

# Compila o projeto
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/ControleDeFrequencia-0.0.1-SNAPSHOT.jar"]