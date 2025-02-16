package padroescriacao.exercicio01.bridge;

import padroescriacao.exercicio01.singleton.Logger;

public class TextoSimplesMessage implements Message {
    private final Sender sender;
    private final String content;

    private TextoSimplesMessage(Sender sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public static TextoSimplesMessage create(Sender sender, String content) {
        return new TextoSimplesMessage(sender, content);
    }

    @Override
    public String send() {
        String result = sender.sendMessage("Texto Simples: " + content);
        Logger.getInstance().log("Mensagem Texto Simples enviada.");
        return result;
    }
}