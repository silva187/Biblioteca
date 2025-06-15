package controller;

import java.util.Scanner;

import service.UsuarioService;

public class UsuarioController {

    public void fazerLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        UsuarioService service = new UsuarioService();
        boolean sucesso = service.autenticar(login, senha);

        if (sucesso) {
            System.out.println("Login bem-sucedido!");
        } else {
            System.out.println("Login inválido.");
        }
    }
}
