package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String url = "jdbc:postgresql://localhost:5432/biblioteca";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(url , USUARIO, SENHA);
            System.out.println("Conectado ao banco com sucesso!");
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
}