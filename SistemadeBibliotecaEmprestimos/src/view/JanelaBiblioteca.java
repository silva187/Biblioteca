package view;

import javax.swing.*;

import service.UsuarioService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaBiblioteca extends JFrame {

    private JLabel entraUsuario;
    private JTextField campoUsuario;
    private JLabel entraSenha;
    private JPasswordField campoSenha;
    private JButton jbutton;
    private String usuario;
    private String senha;

    public JanelaBiblioteca() {

        setTitle("BIBLIOTECA - CADASTRO");
        setSize(370, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(null);
        getContentPane().setBackground(new Color(0x00, 0x66, 0x60));

        ImageIcon alunoOriginal = new ImageIcon(getClass().getResource("/view/aluno.png"));
        Image imagemRedimensionada = alunoOriginal.getImage().getScaledInstance(108, 108, Image.SCALE_SMOOTH);
        JLabel alunoLabel = new JLabel(new ImageIcon(imagemRedimensionada));
        alunoLabel.setBounds(125, 30, 120, 120);
        alunoLabel.setBorder(BorderFactory.createLineBorder(new Color(0x00, 0x8D, 0x85), 2));
        add(alunoLabel);

        entraUsuario = new JLabel("Login:");
        entraUsuario.setBounds(45, 190, 100, 30);
        entraUsuario.setFont(new Font("Fonte Serif", Font.BOLD, 15));
        entraUsuario.setForeground(new Color(0, 0, 0));

        campoUsuario = new JTextField();
        campoUsuario.setBounds(95, 190, 180, 30);
        add(entraUsuario);
        add(campoUsuario);

        entraSenha = new JLabel("Senha:");
        entraSenha.setBounds(41, 240, 100, 30);
        entraSenha.setFont(new Font("Fonte Serif", Font.BOLD, 15));
        entraSenha.setForeground(new Color(0, 0, 0));

        campoSenha = new JPasswordField();
        campoSenha.setBounds(95, 240, 180, 30);
        add(campoSenha);
        add(entraSenha);

        jbutton = new JButton("Entrar");
        jbutton.setBounds(135, 300, 100, 35);
        jbutton.setFont(new Font("Arial", Font.BOLD, 14));

        add(jbutton);

        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuario = campoUsuario.getText();
                senha = new String(campoSenha.getPassword());

                UsuarioService usuarioService = new UsuarioService();
                boolean autenticado = usuarioService.autenticar(usuario, senha);

                if (autenticado) {
                    JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
                    new JanelaEmprestimo(usuario);
                    //dispose(); // fecha a janela de login
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Login ou senha incorretos!",
                            "Erro de autenticação",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        setVisible(true);
    }
}