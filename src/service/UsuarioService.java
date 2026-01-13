package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Conexao;

public class UsuarioService {

    public boolean autenticar(String usuario, String senha) {
        Connection conn = Conexao.conectar();
        if (conn == null) return false;

        String sql = "SELECT * FROM aluno WHERE usuario = ? AND senha = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // se encontrou o usuário, login é válido
            }
        } catch (SQLException e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}