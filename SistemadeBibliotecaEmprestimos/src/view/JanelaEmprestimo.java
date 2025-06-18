package view;

import model.Aluno;
import model.Biblioteca;
import model.Emprestimo;
import model.Livro;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JanelaEmprestimo extends JFrame {

    private JTextField campoLivro;
    private JLabel statusLabel;
    private JLabel statusAlunoLabel;
    private JButton renovarBtn;
    private JButton cancelarBtn;
    private JScrollPane scrollPane;
    private JLabel labelLivro;
    private JButton verificarBtn;
    private JButton solicitarBtn;
    private boolean livroDisponivel = false;

    private LocalDateTime agora = LocalDateTime.now();
    private LocalDateTime renovadoLimite = agora.plusDays(10);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy");

    private String nomeAlunoAtual = "";
    private String entrada;

    private final List<Emprestimo> emprestimosAtuais = new ArrayList<>();
    private Aluno aluno;
    private Biblioteca biblioteca = new Biblioteca();

    public JanelaEmprestimo(String usuario) {
        setTitle("Solicitar Empréstimo");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(0x00, 0x66, 0x60));

        labelLivro = new JLabel("Livro:");
        labelLivro.setBounds(30, 30, 100, 30);
        labelLivro.setForeground(new Color(0, 0, 0));
        labelLivro.setFont(new Font("Fonte Serif", Font.BOLD, 15));
        add(labelLivro);

        campoLivro = new JTextField();
        campoLivro.setBounds(90, 30, 400, 30);
        add(campoLivro);

        verificarBtn = new JButton("Verificar");
        verificarBtn.setBounds(150, 80, 100, 30);
        verificarBtn.setForeground(new Color(0, 0, 0));
        add(verificarBtn);

        solicitarBtn = new JButton("Solicitar Empréstimo");
        solicitarBtn.setBounds(270, 80, 180, 30);
        solicitarBtn.setForeground(new Color(0, 0, 0));
        add(solicitarBtn);

        statusLabel = new JLabel("Status: Aguardando...");
        statusLabel.setBounds(30, 130, 450, 30);
        statusLabel.setForeground(new Color(0, 0, 0));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(statusLabel);

        statusAlunoLabel = new JLabel("<html>Status do Aluno: ---</html>");
        statusAlunoLabel.setForeground(new Color(0, 0, 0));
        statusAlunoLabel.setFont(new Font("Arial", Font.BOLD, 15));
        statusAlunoLabel.setVerticalAlignment(SwingConstants.TOP);

        scrollPane = new JScrollPane(statusAlunoLabel);
        scrollPane.setBounds(30, 160, 500, 170);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        add(scrollPane);

        renovarBtn = new JButton("Renovar Empréstimo");
        renovarBtn.setBounds(80, 350, 170, 30);
        renovarBtn.setForeground(new Color(0, 0, 0));
        renovarBtn.setVisible(false);
        add(renovarBtn);

        cancelarBtn = new JButton("Cancelar Empréstimo");
        cancelarBtn.setBounds(280, 350, 170, 30);
        cancelarBtn.setForeground(new Color(0, 0, 0));
        cancelarBtn.setVisible(false);
        add(cancelarBtn);

        verificarBtn.addActionListener(e -> {
            entrada = campoLivro.getText().trim();
            boolean formatoValido = entrada.matches("^[A-Za-zÀ-ÿ\\s]+,\\s[A-Za-zÀ-ÿ\\s]+\\.LIV\\d{3}$");
            if (entrada.isEmpty()) {
                statusLabel.setText("Status: Informe os dados do livro.");
                return;
            }
            Livro livroEncontrado = Livro.buscarLivroNoCatalogo(entrada);
            if (livroEncontrado == null) {
                statusLabel.setText("Status: Livro não encontrado ou formato inválido. Use: Título, Autor.12345");
                livroDisponivel = false;
            } else if (livroEncontrado.isDisponivel()) {
                statusLabel.setText("Status: Livro \"" + livroEncontrado.getTitulo() + "\" disponível para empréstimo.");
                livroDisponivel = true;
            } else {
                statusLabel.setText("Status: Livro \"" + livroEncontrado.getTitulo() + "\" não disponível (já emprestado).");
                livroDisponivel = false;
            }
        });

        solicitarBtn.addActionListener(e -> {
            if (!livroDisponivel) {
                JOptionPane.showMessageDialog(null, "Você deve verificar o livro antes de solicitar o empréstimo.");
                return;
            }

            Livro livro = Livro.buscarLivroNoCatalogo(entrada);
            if (livro == null) {
                statusLabel.setText("Status: Livro não encontrado.");
                return;
            }

            aluno = Aluno.buscarPorUsuario(usuario);

            if (aluno == null) {
                JOptionPane.showMessageDialog(null, "Aluno não encontrado.");
                return;
            }

            if (!aluno.podeEmprestar()) {
                JOptionPane.showMessageDialog(null, "Você não pode realizar novos empréstimos: limite atingido ou há empréstimos vencidos.");
                return;
            }

            if (aluno.temEmprestimoAtivo(livro)) {
                JOptionPane.showMessageDialog(null, "Você já possui esse livro emprestado.", "Empréstimo Repetido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Emprestimo emprestimo = new Emprestimo(livro, aluno);
            boolean sucesso = emprestimo.registrar();

            if (sucesso) {
                aluno.adicionarEmprestimo(emprestimo);
                nomeAlunoAtual = aluno.getNome();
                emprestimosAtuais.add(emprestimo);
                atualizarStatusAluno();
                renovarBtn.setVisible(true);
                cancelarBtn.setVisible(true);
                statusLabel.setText("Status: Empréstimo registrado com sucesso.");
            } else {
                statusLabel.setText("Status: Falha ao registrar empréstimo.");
            }
        });

        renovarBtn.addActionListener(e -> {
            if (emprestimosAtuais.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum empréstimo para renovar.");
                return;
            }

            Object[] opcoes = emprestimosAtuais.stream()
                    .map(emp -> emp.getLivro().getTitulo())
                    .toArray();

            String tituloEscolhido = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione o livro que deseja renovar:",
                    "Renovar Empréstimo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (tituloEscolhido != null) {
                Emprestimo escolhido = emprestimosAtuais.stream()
                        .filter(emp -> emp.getLivro().getTitulo().equals(tituloEscolhido))
                        .findFirst()
                        .orElse(null);

                if (escolhido != null) {
                    boolean sucesso = escolhido.renovarEmprestimo();

                    if (sucesso) {
                        atualizarStatusAluno();
                        JOptionPane.showMessageDialog(null, "Renovado com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Não foi possível renovar o empréstimo.");
                    }
                }
            }
        });

        cancelarBtn.addActionListener(e -> {
            if (emprestimosAtuais.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum empréstimo para cancelar.");
                return;
            }

            Object[] opcoes = emprestimosAtuais.stream()
                    .map(emp -> emp.getLivro().getTitulo())
                    .toArray();

            String tituloEscolhido = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione o empréstimo (livro) para cancelar:",
                    "Cancelar Empréstimo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (tituloEscolhido != null) {
                Emprestimo escolhido = emprestimosAtuais.stream()
                        .filter(emp -> emp.getLivro().getTitulo().equals(tituloEscolhido))
                        .findFirst()
                        .orElse(null);

                if (escolhido != null) {
                    boolean sucesso = escolhido.cancelarEmprestimo();

                    if (sucesso) {
                        emprestimosAtuais.remove(escolhido);
                        atualizarStatusAluno();
                        JOptionPane.showMessageDialog(null, "Cancelado com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Não foi possível cancelar o empréstimo.");
                    }
                }
            }
        });

        aluno = Aluno.buscarPorUsuario(usuario);
        if (aluno != null) {
            List<String> avisos = biblioteca.emitirAvisosVencimentoProximoDoAluno(aluno);
            avisos.forEach(m -> JOptionPane.showMessageDialog(this, m, "Vencimento Próximo", JOptionPane.WARNING_MESSAGE));
        }

        setVisible(true);
    }

    private void atualizarStatusAluno() {
        statusAlunoLabel.setText(biblioteca.gerarResumoDoAluno(aluno));
    }
}
