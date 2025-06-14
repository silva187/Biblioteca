# Use a imagem com JDK
FROM openjdk:17

# Instala utilitários gráficos
RUN apt-get update && apt-get install -y x11-apps

# Define diretório de trabalho
WORKDIR /app

# Copia tudo para dentro do container
COPY . /app

# Compila todos os arquivos Java
RUN javac model/*.java view/*.java Main.java

# Comando padrão para rodar
CMD ["java", "Main"]
