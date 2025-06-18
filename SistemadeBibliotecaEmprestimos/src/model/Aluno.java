package model;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private String nome;
    private String matricula;
    private List<Emprestimo> listaEmprestimos;

    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.listaEmprestimos = new ArrayList<>();
    }

    public boolean podeEmprestar() {
        // Um aluno não pode emprestar se: já tem 3 empréstimos ativos ou tem algum vencido
        return listaEmprestimos.size() < 3 &&
                listaEmprestimos.stream().noneMatch(Emprestimo::estaVencido);
    }

    public static Aluno buscarPorUsuario(String usuario) {
        service.UsuarioService usuarioService = new service.UsuarioService();
        return usuarioService.buscarAlunoPorUsuario(usuario);
    }

    public void adicionarEmprestimo(Emprestimo e) {
        listaEmprestimos.add(e);
    }

    public void removerEmprestimo(Emprestimo e) {
        listaEmprestimos.remove(e);
    }

    public boolean temEmprestimoAtivo(Livro livro) {
        return listaEmprestimos.stream().anyMatch(e -> e.getLivro().getCodigo().equals(livro.getCodigo()));
    }

    public List<Emprestimo> getEmprestimos() {
        return listaEmprestimos;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }
}