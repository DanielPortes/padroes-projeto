package padroescriacao.exercicio01.bridge;

public class SMSSender implements Sender {
    @Override
    public String sendMessage(String content) {
        return "SMS enviado: " + content;
    }
}
