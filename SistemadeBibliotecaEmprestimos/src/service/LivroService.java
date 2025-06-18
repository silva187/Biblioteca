package service;

import database.Conexao;
import model.Livro;

import java.sql.*;

public class LivroService {

        public Livro buscarPorTituloAutorECodigo(String titulo, String autor, String codigo) {
            String sql = "SELECT * FROM livro WHERE titulo ILIKE ? AND autor ILIKE ? AND codigo = ?";

            try (Connection conn = Conexao.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + titulo + "%");
                stmt.setString(2, "%" + autor + "%");
                stmt.setString(3, codigo);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Livro(
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("codigo"),
                            rs.getBoolean("disponivel")
                    );
                }

            } catch (Exception e) {
                System.out.println("Erro ao buscar livro por título, autor e código: " + e.getMessage());
            }

            return null;
        }
}

