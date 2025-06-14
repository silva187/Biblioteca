package model;

import java.util.*;

public class Biblioteca {
    private List<Livro> listaLivros;
    private List<Aluno> listaAlunos;
    private Queue<Emprestimo> filaEmprestimos;

    public Biblioteca() {
        this.listaLivros = new ArrayList<>();
        this.listaAlunos = new ArrayList<>();
        this.filaEmprestimos = new LinkedList<>();
    }

    public void adicionarLivro(Livro livro) {
        listaLivros.add(livro);
    }

    public void cadastrarAluno(Aluno aluno) {
        listaAlunos.add(aluno);
    }

    public void emprestarLivro(Aluno aluno, Livro livro) {
        if (!livro.isDisponivel()) {
            System.out.println("Livro não está disponível.");
            return;
        }

        if (!aluno.podeEmprestar()) {
            System.out.println("Aluno não pode emprestar mais livros.");
            return;
        }

        Emprestimo emprestimo = new Emprestimo(livro, aluno);
        aluno.adicionarEmprestimo(emprestimo);
        livro.marcarComoEmprestado();
        filaEmprestimos.add(emprestimo);
    }

    public void devolverLivro(Emprestimo emprestimo) {
        emprestimo.getAluno().removerEmprestimo(emprestimo);
        emprestimo.getLivro().marcarComoDisponivel();
        filaEmprestimos.remove(emprestimo);
    }

    public void emitirAvisos() {
        for (Emprestimo e : filaEmprestimos) {
            if (e.estaVencido()) {
                System.out.println("⚠️ Atraso! Livro: " + e.getLivro().getTitulo() + 
                                   " - Aluno: " + e.getAluno().getNome());
            }
        }
    }

    public List<Emprestimo> listarTodosEmprestimos() {
        return new ArrayList<>(filaEmprestimos);
    }

    public List<Livro> getListaLivros() {
        return listaLivros;
    }

    public List<Aluno> getListaAlunos() {
        return listaAlunos;
    }
}
