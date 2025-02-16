package padroescriacao.exercicio01;

import org.junit.jupiter.api.Test;
import padroescriacao.exercicio01.bridge.EmailSender;
import padroescriacao.exercicio01.bridge.Sender;

import static org.junit.jupiter.api.Assertions.*;

public class EmailSenderTest {

    @Test
    public void testSendMessage() {
        Sender emailSender = new EmailSender();
        String result = emailSender.sendMessage("Teste de Email");

        assertEquals("Email enviado: Teste de Email", result, "A mensagem enviada pelo EmailSender deve estar formatada corretamente.");
    }

    @Test
    public void testSendMessageWithNullContent() {
        Sender emailSender = new EmailSender();
        String result = emailSender.sendMessage(null);

        assertEquals("Email enviado: null", result, "O EmailSender deve lidar com mensagens nulas.");
    }

    @Test
    public void testSendMessageWithEmptyContent() {
        Sender emailSender = new EmailSender();
        String result = emailSender.sendMessage("");

        assertEquals("Email enviado: ", result, "O EmailSender deve lidar com mensagens vazias.");
    }
}
