package model;

import service.EmprestimoService;
import service.LivroService;
import service.UsuarioService;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Biblioteca {
    private List<Livro> listaLivros = new ArrayList<>();
    private List<Aluno> listaAlunos = new ArrayList<>();
    private List<Emprestimo> listaEmprestimosAtivos = new ArrayList<>();
    private EmprestimoService emprestimoService;
    private LivroService livroService;
    private UsuarioService usuarioService;

    public Biblioteca() {
        this.emprestimoService = new EmprestimoService();
        this.livroService = new LivroService();
        this.usuarioService = new UsuarioService();
    }

    public List<String> emitirAvisosVencimentoProximoDoAluno(Aluno aluno) {
        List<String> mensagens = new ArrayList<>();
        List<Emprestimo> emprestimos = emprestimoService.buscarTodosEmprestimos();
        if (emprestimos != null) {
            this.listaEmprestimosAtivos = emprestimos;
        }

        for (Emprestimo e : listaEmprestimosAtivos) {
            if (e.getAluno().getMatricula().equals(aluno.getMatricula()) && e.estaPertoDoVencimento(3)) {
                mensagens.add("⚠ Vencimento em breve!\nLivro: " + e.getLivro().getTitulo() +
                        "\nData: " + e.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }
        return mensagens;
    }

    public String gerarResumoDoAluno(Aluno aluno) {
        StringBuilder builder = new StringBuilder("<html><b>Status do Aluno:</b><br>");
        builder.append("Aluno: ").append(aluno.getNome()).append("<br>");
        builder.append("Livros emprestados:<br>");

        for (Emprestimo emp : aluno.getEmprestimos()) {
            builder.append("- ").append(emp.getLivro().getTitulo())
                    .append(" (autor: ").append(emp.getLivro().getAutor()).append(")<br>")
                    .append("&nbsp;&nbsp;Empréstimo: ").append(emp.getDataEmprestimo().format(DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy"))).append("<br>")
                    .append("&nbsp;&nbsp;Devolução: ").append(emp.getDataLimite().format(DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy"))).append("<br>");
        }

        builder.append("</html>");
        return builder.toString();
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return new ArrayList<>(listaEmprestimosAtivos);
    }

    public List<Livro> getListaLivros() {
        return listaLivros;
    }

    public List<Aluno> getListaAlunos() {
        return listaAlunos;
    }
}