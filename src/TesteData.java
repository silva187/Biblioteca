
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class TesteData {
    public static void main(String[] args) {
        // Data/hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Adiciona 7 dias
        LocalDateTime maisSeteDias = agora.plusDays(10);

        // Formata para exibição
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd/MM/yyyy");
        String atualFormatado = agora.format(formatter);
        String futuraFormatada = maisSeteDias.format(formatter);

        // Exibe os resultados
        System.out.println("Data atual: " + atualFormatado);
        System.out.println("Data + 10 dias: " + futuraFormatada);
    }
}
