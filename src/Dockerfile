# Use a imagem com JDK
FROM openjdk:17

# Instala utilitários gráficos (se necessário para testes ou uso no contêiner)
RUN apt-get update && apt-get install -y x11-apps

# Define diretório de trabalho
WORKDIR /app

# Copia apenas os arquivos necessários para a construção do projeto
COPY ./src /app/src
COPY ./Main.java /app

# Compila todos os arquivos Java
RUN javac src/model/*.java src/view/*.java Main.java

# Comando para rodar a aplicação
CMD ["java", "Main"]
