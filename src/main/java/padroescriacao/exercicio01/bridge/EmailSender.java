package padroescriacao.exercicio01.bridge;

public class EmailSender implements Sender {
    @Override
    public String sendMessage(String content) {
        return "Email enviado: " + content;
    }
}
