package padroescriacao.exercicio01;

import org.junit.jupiter.api.Test;
import padroescriacao.exercicio01.bridge.*;
import padroescriacao.exercicio01.factory.MessageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFactoryTest {

    @Test
    public void testCreateHTMLMessage() {
        Sender emailSender = new EmailSender();
        Message message = MessageFactory.createMessage("HTML", emailSender, "Conteúdo HTML");

        assertTrue(message instanceof HTMLMessage, "Deve retornar uma instância de HTMLMessage.");
        assertEquals("Email enviado: HTML: <html>Conteúdo HTML</html>", message.send(), "A mensagem HTML deve ser enviada corretamente.");
    }

    @Test
    public void testCreateTextoSimplesMessage() {
        Sender smsSender = new SMSSender();
        Message message = MessageFactory.createMessage("TextoSimples", smsSender, "Conteúdo Simples");

        assertTrue(message instanceof TextoSimplesMessage, "Deve retornar uma instância de TextoSimplesMessage.");
        assertEquals("SMS enviado: Texto Simples: Conteúdo Simples", message.send(), "A mensagem Texto Simples deve ser enviada corretamente.");
    }

    @Test
    public void testUnknownMessageType() {
        Sender emailSender = new EmailSender();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            MessageFactory.createMessage("Invalido", emailSender, "Erro");
        });

        assertTrue(exception.getMessage().contains("Erro ao criar mensagem do tipo"), "Deve lançar exceção para tipo desconhecido.");
    }

    @Test
    public void testCreateMessageWithInvalidType() {
        Sender sender = new EmailSender();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            MessageFactory.createMessage("Invalido", sender, "Conteúdo inválido");
        });

        String expectedMessage = "Erro ao criar mensagem do tipo";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                "Deve lançar RuntimeException ao tentar criar um tipo desconhecido.");
    }

}
