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
        if (listaEmprestimos.size() >= 3) return false;

        for (Emprestimo e : listaEmprestimos) {
            if (e.estaVencido()) return false;
        }

        return true;
    }

    public void adicionarEmprestimo(Emprestimo e) {
        listaEmprestimos.add(e);
    }

    public void removerEmprestimo(Emprestimo e) {
        listaEmprestimos.remove(e);
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
