package model;

import service.LivroService;

public class Livro {
    private String titulo;
    private String autor;
    private String codigo;
    private boolean disponivel;

    public Livro(String titulo, String autor, String codigo, boolean disponivel) {
        this.titulo = titulo;
        this.autor = autor;
        this.codigo = codigo;
        this.disponivel = disponivel;
    }

    public static Livro buscarLivroNoCatalogo(String entrada) {
        try {
            String[] partes = entrada.split("\\.");
            if (partes.length != 2) return null;

            String[] tituloAutor = partes[0].split(",\\s");
            if (tituloAutor.length != 2) return null;

            String titulo = tituloAutor[0].trim();
            String autor = tituloAutor[1].trim();
            String codigo = partes[1].trim();

            LivroService livroService = new LivroService();
            return livroService.buscarPorTituloAutorECodigo(titulo, autor, codigo);
        } catch (Exception e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
            return null;
        }
    }

    public void marcarComoDisponivel() {
        this.disponivel = true;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCodigo() {
        return codigo;
    }
}