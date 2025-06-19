package service;

import database.Conexao;
import model.Aluno;
import model.Emprestimo;
import model.Livro;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoService {

    public boolean registrarEmprestimo(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (livro_id, aluno_id, data_emprestimo, hora_emprestimo, data_limite, renovacoes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int alunoId = buscarIdAluno(conn, emprestimo.getAluno().getMatricula());
            int livroId = buscarIdLivro(conn, emprestimo.getLivro().getCodigo());

            if (alunoId == -1 || livroId == -1) {
                System.out.println("Aluno ou Livro não encontrado.");
                return false;
            }

            stmt.setInt(1, livroId);
            stmt.setInt(2, alunoId);
            stmt.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo().toLocalDate()));
            stmt.setTime(4, Time.valueOf(emprestimo.getDataEmprestimo().toLocalTime()));
            stmt.setDate(5, Date.valueOf(emprestimo.getDataLimite().toLocalDate()));
            stmt.setInt(6, emprestimo.getRenovacoes());

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                marcarLivroComoEmprestado(conn, livroId);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar empréstimo: " + e.getMessage());
        }

        return false;
    }
    public List<Emprestimo> buscarTodosEmprestimos() {
        List<Emprestimo> emprestimos = new ArrayList<>();

        String sql = "SELECT e.data_emprestimo, e.hora_emprestimo, e.data_limite, e.renovacoes, " +
                "l.codigo, l.titulo, l.autor, l.disponivel, a.nome, a.matricula " +
                "FROM emprestimo e " +
                "JOIN livro l ON e.livro_id = l.id " +
                "JOIN aluno a ON e.aluno_id = a.id";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("codigo"),
                        rs.getBoolean("disponivel")
                );

                Aluno aluno = new Aluno(
                        rs.getString("nome"),
                        rs.getString("matricula")
                );

                LocalDateTime dataEmprestimo = rs.getDate("data_emprestimo").toLocalDate()
                        .atTime(rs.getTime("hora_emprestimo").toLocalTime());

                LocalDateTime dataLimite = rs.getDate("data_limite").toLocalDate().atStartOfDay();

                int renovacoes = rs.getInt("renovacoes");

                Emprestimo emprestimo = new Emprestimo(livro, aluno, dataEmprestimo, dataLimite, renovacoes);
                emprestimos.add(emprestimo);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar todos os empréstimos: " + e.getMessage());
        }

        return emprestimos;
    }
    public List<Emprestimo> buscarEmprestimosDoAluno(String matricula) {
        List<Emprestimo> emprestimos = new ArrayList<>();

        String sql = "SELECT e.data_emprestimo, e.hora_emprestimo, e.data_limite, e.renovacoes, " +
                "l.codigo, l.titulo, l.autor, l.disponivel, a.nome, a.matricula " +
                "FROM emprestimo e " +
                "JOIN livro l ON e.livro_id = l.id " +
                "JOIN aluno a ON e.aluno_id = a.id " +
                "WHERE a.matricula = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("codigo"),
                        rs.getBoolean("disponivel")
                );

                Aluno aluno = new Aluno(
                        rs.getString("nome"),
                        rs.getString("matricula")
                );

                LocalDateTime dataEmprestimo = rs.getDate("data_emprestimo").toLocalDate()
                        .atTime(rs.getTime("hora_emprestimo").toLocalTime());
                LocalDateTime dataLimite = rs.getDate("data_limite").toLocalDate().atStartOfDay();
                int renovacoes = rs.getInt("renovacoes");

                Emprestimo emprestimo = new Emprestimo(livro, aluno, dataEmprestimo, dataLimite, renovacoes);
                emprestimos.add(emprestimo);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar empréstimos do aluno: " + e.getMessage());
        }

        return emprestimos;
    }


    public boolean renovarEmprestimo(Emprestimo emprestimo) {
        String sql = "UPDATE emprestimo SET data_limite = ?, renovacoes = ? WHERE aluno_id = ? AND livro_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int alunoId = buscarIdAluno(conn, emprestimo.getAluno().getMatricula());
            int livroId = buscarIdLivro(conn, emprestimo.getLivro().getCodigo());

            if (emprestimo.getRenovacoes() >= 2) {
                System.out.println("Limite de renovações atingido.");
                return false;
            }

            LocalDate novaDataLimite = LocalDate.from(emprestimo.getDataLimite().plusDays(10));
            int novasRenovacoes = emprestimo.getRenovacoes() + 1;

            stmt.setDate(1, Date.valueOf(novaDataLimite));
            stmt.setInt(2, novasRenovacoes);
            stmt.setInt(3, alunoId);
            stmt.setInt(4, livroId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao renovar empréstimo: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelarEmprestimo(Emprestimo emprestimo) {
        String sql = "DELETE FROM emprestimo WHERE aluno_id = ? AND livro_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int alunoId = buscarIdAluno(conn, emprestimo.getAluno().getMatricula());
            int livroId = buscarIdLivro(conn, emprestimo.getLivro().getCodigo());

            stmt.setInt(1, alunoId);
            stmt.setInt(2, livroId);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                marcarLivroComoDisponivel(conn, livroId);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cancelar empréstimo: " + e.getMessage());
        }

        return false;
    }

    private int buscarIdAluno(Connection conn, String matricula) throws SQLException {
        String sql = "SELECT id FROM aluno WHERE matricula = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    private int buscarIdLivro(Connection conn, String codigo) throws SQLException {
        String sql = "SELECT id FROM livro WHERE codigo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }

    private void marcarLivroComoEmprestado(Connection conn, int livroId) throws SQLException {
        String sql = "UPDATE livro SET disponivel = FALSE WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.executeUpdate();
        }
    }

    private void marcarLivroComoDisponivel(Connection conn, int livroId) throws SQLException {
        String sql = "UPDATE livro SET disponivel = TRUE WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, livroId);
            stmt.executeUpdate();
        }
    }
}

