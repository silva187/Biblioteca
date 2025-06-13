import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JanelaEmprestimo extends JFrame {

    private JTextField campoLivro;
    private JLabel statusLabel;
    private JLabel statusAlunoLabel;
    private JButton renovarBtn;
    private JButton cancelarBtn;


    private LocalDateTime agora = LocalDateTime.now();
    private LocalDateTime renovadoLimite = agora.plusDays(10);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy");

    private String emprestado = agora.format(formatter);
    private String renovado = renovadoLimite.format(formatter);

    private String nomeAlunoAtual = "";
    private String nomeLivroAtual = "";
    private String entrada;

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
        statusAlunoLabel.setBounds(30, 170, 500, 100); // altura ajustada
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
                nomeAlunoAtual = usuario;
                nomeLivroAtual = entrada;


                agora = LocalDateTime.now();
                emprestado = agora.format(formatter);
                renovadoLimite = agora.plusDays(10);
                renovado = renovadoLimite.format(formatter);

                statusAlunoLabel.setText(
                        "<html><b>Status do Aluno:</b><br>" +
                        "Aluno: " + nomeAlunoAtual + "<br>" +
                        "Livro: " + nomeLivroAtual + "<br>" +
                        "Empréstimo realizado: " + emprestado + "<br>" +
                        "Empréstimo limite: " + renovado +
                        "</html>");

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
            renovado = renovadoLimite.format(formatter);

            statusAlunoLabel.setText(
                    "<html><b>Status do Aluno:</b><br>" +
                    "Aluno: " + nomeAlunoAtual + "<br>" +
                    "Livro: " + nomeLivroAtual + "<br>" +
                    "Empréstimo realizado: " + emprestado + "<br>" +
                    "Empréstimo limite: " + renovado +
                    "</html>");

            JOptionPane.showMessageDialog(null, "Empréstimo renovado com sucesso.");
        });

        cancelarBtn.addActionListener(e -> {
            statusAlunoLabel.setText("<html><b>Status do Aluno:</b><br>Empréstimo cancelado.</html>");
            renovarBtn.setVisible(false);
            cancelarBtn.setVisible(false);
        });

        setVisible(true);
    }
}
