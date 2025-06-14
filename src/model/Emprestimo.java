package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private Livro livro;
    private Aluno aluno;
    private LocalDate dataEmprestimo;
    private LocalTime horaEmprestimo;
    private LocalDate dataLimite;
    private int renovacoes;

    public Emprestimo(Livro livro, Aluno aluno) {
        this.livro = livro;
        this.aluno = aluno;
        this.dataEmprestimo = LocalDate.now();
        this.horaEmprestimo = LocalTime.now();
        this.dataLimite = dataEmprestimo.plusDays(10);
        this.renovacoes = 0;
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(dataLimite);
    }

    public int diasRestantes() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), dataLimite);
    }

    public void renovar() {
        if (renovacoes < 2) {
            dataLimite = dataLimite.plusDays(10);
            renovacoes++;
        }
    }

    public Livro getLivro() {
        return livro;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalTime getHoraEmprestimo() {
        return horaEmprestimo;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public int getRenovacoes() {
        return renovacoes;
    }
}
