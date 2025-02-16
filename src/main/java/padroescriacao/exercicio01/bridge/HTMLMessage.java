package padroescriacao.exercicio01.bridge;

import padroescriacao.exercicio01.singleton.Logger;

public class HTMLMessage implements Message {
    private final Sender sender;
    private final String htmlContent;

    private HTMLMessage(Sender sender, String htmlContent) {
        this.sender = sender;
        this.htmlContent = htmlContent;
    }

    public static HTMLMessage create(Sender sender, String htmlContent) {
        return new HTMLMessage(sender, htmlContent);
    }

    @Override
    public String send() {
        String result = sender.sendMessage("HTML: <html>" + htmlContent + "</html>");
        Logger.getInstance().log("Mensagem HTML enviada.");
        return result;
    }
}