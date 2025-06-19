package model;

import service.EmprestimoService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Emprestimo {
    private Livro livro;
    private Aluno aluno;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataLimite;
    private int renovacoes;

    public Emprestimo(Livro livro, Aluno aluno) {
        this.livro = livro;
        this.aluno = aluno;
        this.dataEmprestimo = LocalDateTime.now();
        this.dataLimite = dataEmprestimo.plusDays(10);
        this.renovacoes = 0;
    }
    public Emprestimo(Livro livro, Aluno aluno, LocalDateTime dataEmprestimo, LocalDateTime dataLimite, int renovacoes) {
        this.livro = livro;
        this.aluno = aluno;
        this.dataEmprestimo = dataEmprestimo;
        this.dataLimite = dataEmprestimo;
        this.renovacoes = renovacoes;
    }

    public boolean estaVencido() {
        return LocalDateTime.now().isAfter(dataLimite);
    }

    public boolean estaPertoDoVencimento(int dias) {
        long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), dataLimite);
        return diasRestantes >= 0 && diasRestantes <= dias;
    }

    public boolean registrar() {
        EmprestimoService service = new EmprestimoService();
        return service.registrarEmprestimo(this);
    }

    public boolean renovarEmprestimo() {
        if (this.renovacoes >= 1) {
            System.out.println("Limite de renovações atingido.");
            return false;
        }

        EmprestimoService service = new EmprestimoService();
        boolean sucesso = service.renovarEmprestimo(this);

        if (sucesso) {
            this.renovar();
        }

        return sucesso;
    }

    public boolean cancelarEmprestimo() {
        EmprestimoService service = new EmprestimoService();
        boolean sucesso = service.cancelarEmprestimo(this);

        if (sucesso) {
            aluno.removerEmprestimo(this);
            livro.marcarComoDisponivel();
        }

        return sucesso;
    }

    public void renovar() {
        this.dataLimite = this.dataLimite.plusDays(10);
        this.renovacoes++;
    }

    public static List<Emprestimo> carregarEmprestimosDoAluno(Aluno aluno) {
        EmprestimoService service = new EmprestimoService();
        List<Emprestimo> emprestimos = service.buscarEmprestimosDoAluno(aluno.getMatricula());

        for (Emprestimo emp : emprestimos) {
            aluno.adicionarEmprestimo(emp);
        }

        return emprestimos;
    }

    public Livro getLivro() {
        return livro;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    public int getRenovacoes() {
        return renovacoes;
    }
}