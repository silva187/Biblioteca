package service;

import database.Conexao;
import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroService {

        public Livro buscarPorTituloEAutor(String titulo, String autor) {
            String sql = "SELECT * FROM livro WHERE titulo ILIKE ? AND autor ILIKE ?";
            try (Connection conn = Conexao.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, "%" + titulo.trim() + "%");
                stmt.setString(2, "%" + autor.trim() + "%");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Livro(
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("codigo"),
                            rs.getBoolean("disponivel"));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar livro: " + e.getMessage());
            }
            return null;
        }

        public List<Livro> buscarLivrosDisponiveis() {
            List<Livro> livros = new ArrayList<>();
            String sql = "SELECT * FROM livro WHERE disponivel = TRUE";
            try (Connection conn = Conexao.conectar();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    livros.add(new Livro(
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("codigo"),
                            rs.getBoolean("disponivel")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar livros disponíveis: " + e.getMessage());
            }
            return livros;
        }


        public Livro buscarPorCodigo(String codigo) {
                String sql = "SELECT * FROM livro WHERE codigo = ?";
                try (Connection conn = Conexao.conectar();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, codigo);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        return new Livro(
                                rs.getString("titulo"),
                                rs.getString("autor"),
                                rs.getString("codigo"),
                                rs.getBoolean("disponivel")
                        );
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao buscar livro por código: " + e.getMessage());
                }
                return null;
            }


        public boolean marcarComoEmprestado(String codigo) {
                String sql = "UPDATE livro SET disponivel = FALSE WHERE codigo = ?";
                try (Connection conn = Conexao.conectar();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, codigo);
                    return stmt.executeUpdate() > 0;
                } catch (SQLException e) {
                    System.out.println("Erro ao atualizar status do livro: " + e.getMessage());
                    return false;
                }
            }

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
        public boolean removerLivro(String codigo) {
            String sql = "DELETE FROM livro WHERE codigo = ?";
            try (Connection conn = Conexao.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, codigo);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao remover livro: " + e.getMessage());
                return false;
            }
        }
}

