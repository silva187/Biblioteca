package view;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JanelaEmprestimo extends JFrame {

    private JTextField campoLivro;
    private JLabel statusLabel;
    private JLabel statusAlunoLabel;
    private JButton renovarBtn;
    private JButton cancelarBtn;

    private LocalDateTime agora = LocalDateTime.now();
    private LocalDateTime renovadoLimite = agora.plusDays(10);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy");

    private String nomeAlunoAtual = "";
    private String entrada;

    private final ArrayList<String> livrosEmprestados = new ArrayList<>();

    public JanelaEmprestimo(String usuario) {
        setTitle("Solicitar Empréstimo");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel labelLivro = new JLabel("Livro:");
        labelLivro.setBounds(30, 30, 100, 30);
        add(labelLivro);

        campoLivro = new JTextField();
        campoLivro.setBounds(90, 30, 400, 30);
        add(campoLivro);

        JButton verificarBtn = new JButton("Verificar");
        verificarBtn.setBounds(150, 80, 100, 30);
        add(verificarBtn);

        JButton solicitarBtn = new JButton("Solicitar Empréstimo");
        solicitarBtn.setBounds(270, 80, 180, 30);
        add(solicitarBtn);

        statusLabel = new JLabel("Status: Aguardando...");
        statusLabel.setBounds(30, 130, 450, 30);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(statusLabel);

        statusAlunoLabel = new JLabel("Status do Aluno: ---");
        statusAlunoLabel.setBounds(30, 170, 500, 150);
        statusAlunoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(statusAlunoLabel);

        renovarBtn = new JButton("Renovar Empréstimo");
        renovarBtn.setBounds(80, 320, 170, 30);
        renovarBtn.setVisible(false);
        add(renovarBtn);

        cancelarBtn = new JButton("Cancelar Empréstimo");
        cancelarBtn.setBounds(280, 320, 170, 30);
        cancelarBtn.setVisible(false);
        add(cancelarBtn);

        verificarBtn.addActionListener(e -> {
            entrada = campoLivro.getText().trim();
            boolean formatoValido = entrada.matches("^[A-Za-zÀ-ÿ\\s]+,\\s[A-Za-zÀ-ÿ\\s]+\\.\\d{5}$");

            if (entrada.isEmpty()) {
                statusLabel.setText("Status: Informe os dados do livro.");
            } else if (formatoValido) {
                statusLabel.setText("Status: Livro disponível para empréstimo.");
            } else {
                statusLabel.setText("Status: Formato inválido. Use: Título, Autor.12345");
            }
        });

        solicitarBtn.addActionListener(e -> {
            entrada = campoLivro.getText().trim();
            boolean formatoValido = entrada.matches("^.+,\\s.+\\.\\d{5}$");

            if (formatoValido) {
                String livro = entrada;

                if (livrosEmprestados.size() >= 5) {
                    statusLabel.setText("Status: Limite de 5 empréstimos atingido.");
                    return;
                }

                if (livrosEmprestados.contains(livro)) {
                    statusLabel.setText("Status: Este livro já foi emprestado pelo aluno.");
                    return;
                }

                nomeAlunoAtual = usuario;

                agora = LocalDateTime.now();
                String emprestado = agora.format(formatter);
                renovadoLimite = agora.plusDays(10);
                String renovado = renovadoLimite.format(formatter);

                livrosEmprestados.add(livro);
                atualizarStatusAluno(emprestado, renovado);

                renovarBtn.setVisible(true);
                cancelarBtn.setVisible(true);
            } else {
                statusAlunoLabel.setText("Status do Aluno: Formato de entrada inválido.");
                renovarBtn.setVisible(false);
                cancelarBtn.setVisible(false);
            }
        });

        renovarBtn.addActionListener(e -> {
            renovadoLimite = renovadoLimite.plusDays(10);
            String renovado = renovadoLimite.format(formatter);
            String emprestado = agora.format(formatter);

            atualizarStatusAluno(emprestado, renovado);
            JOptionPane.showMessageDialog(null, "Empréstimo renovado com sucesso.");
        });

        cancelarBtn.addActionListener(e -> {
            if (livrosEmprestados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum empréstimo para cancelar.");
                return;
            }

            String[] opcoes = new String[livrosEmprestados.size() + 1];
            for (int i = 0; i < livrosEmprestados.size(); i++) {
                opcoes[i] = livrosEmprestados.get(i);
            }
            opcoes[livrosEmprestados.size()] = "Cancelar todos";

            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione o livro para cancelar:",
                    "Cancelar Empréstimo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha != null) {
                if (escolha.equals("Cancelar todos")) {
                    livrosEmprestados.clear();
                    statusAlunoLabel.setText("<html><b>Status do Aluno:</b><br>Todos os empréstimos foram cancelados.</html>");
                    renovarBtn.setVisible(false);
                    cancelarBtn.setVisible(false);
                } else {
                    livrosEmprestados.remove(escolha);
                    if (livrosEmprestados.isEmpty()) {
                        statusAlunoLabel.setText("<html><b>Status do Aluno:</b><br>Todos os empréstimos foram cancelados.</html>");
                        renovarBtn.setVisible(false);
                        cancelarBtn.setVisible(false);
                    } else {
                        String emprestado = agora.format(formatter);
                        String renovado = renovadoLimite.format(formatter);
                        atualizarStatusAluno(emprestado, renovado);
                    }
                }
            }
        });

        setVisible(true);
    }

    private void atualizarStatusAluno(String emprestado, String renovado) {
        StringBuilder builder = new StringBuilder("<html><b>Status do Aluno:</b><br>");
        builder.append("Aluno: ").append(nomeAlunoAtual).append("<br>");
        builder.append("Livros emprestados:<br>");
        for (String livro : livrosEmprestados) {
            builder.append("- ").append(livro).append("<br>");
        }
        builder.append("Empréstimo realizado: ").append(emprestado).append("<br>");
        builder.append("Empréstimo limite: ").append(renovado).append("</html>");

        statusAlunoLabel.setText(builder.toString());
    }
}
