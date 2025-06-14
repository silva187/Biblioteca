import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaBiblioteca extends JFrame {

    // sem modificações
    private JLabel entraBiblioteca;
    private JLabel entraUsuario;
    private JTextField campoUsuario;
    private JLabel entraSenha;
    private JPasswordField campoSenha;
    private JButton jbutton;
    private String usuario;
    private String senha;

    public JanelaBiblioteca() {

        setTitle("BIBLIOTECA - CADASTRO");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(null);

        entraBiblioteca = new JLabel("ALUNO");
        entraBiblioteca.setBounds(200, 20, 200, 30);
        entraBiblioteca.setFont(new Font("Fonte Serif",Font.BOLD,20));
        add(entraBiblioteca);

        entraUsuario = new JLabel("Login:");
        entraUsuario.setBounds(80, 70, 100, 30);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(150, 70, 180, 30);
        add(entraUsuario);
        add(campoUsuario);

        entraSenha = new JLabel("Senha:");
        entraSenha.setBounds(80, 130, 100, 30);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(150, 130, 180, 30);
        add(campoSenha);
        add(entraSenha);

        jbutton = new JButton("ENTRAR");
        add(jbutton);

        jbutton.setBounds(150, 200, 150, 40);
        jbutton.setFont(new Font("Arial",Font.BOLD,18));
        jbutton.setForeground(new Color(0, 0, 0));
        jbutton.setBackground(new Color(21, 124, 124));


        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuario = campoUsuario.getText();
                senha = new String(campoSenha.getPassword());

                // Verificação do login e senha;
                boolean loginValido = usuario.matches("^[a-z]+\\.[a-z]+\\d{3}$");
                boolean senhaValida = senha.matches("^\\d{4}$");

                if (loginValido && senhaValida) {
                    System.out.printf("Usuário: %s\nSenha: %s\n", usuario, senha);
                    new JanelaEmprestimo(usuario);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Login ou senha inválidos!\nLogin: nome.sobrenome123\nSenha: 4 números.",
                            "Erro de autenticação",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}
