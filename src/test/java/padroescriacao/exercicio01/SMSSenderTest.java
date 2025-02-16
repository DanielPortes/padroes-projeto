package padroescriacao.exercicio01;

import org.junit.jupiter.api.Test;
import padroescriacao.exercicio01.bridge.SMSSender;
import padroescriacao.exercicio01.bridge.Sender;

import static org.junit.jupiter.api.Assertions.*;

public class SMSSenderTest {

    @Test
    public void testSendMessage() {
        Sender smsSender = new SMSSender();
        String result = smsSender.sendMessage("Teste de SMS");

        assertEquals("SMS enviado: Teste de SMS", result, "A mensagem enviada pelo SMSSender deve estar formatada corretamente.");
    }

    @Test
    public void testSendMessageWithNullContent() {
        Sender smsSender = new SMSSender();
        String result = smsSender.sendMessage(null);

        assertEquals("SMS enviado: null", result, "O SMSSender deve lidar com mensagens nulas.");
    }

    @Test
    public void testSendMessageWithEmptyContent() {
        Sender smsSender = new SMSSender();
        String result = smsSender.sendMessage("");

        assertEquals("SMS enviado: ", result, "O SMSSender deve lidar com mensagens vazias.");
    }
}
