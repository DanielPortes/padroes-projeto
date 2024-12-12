package padroescriacao.exercicio01;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlataformaTest {

    @Test
    public void testEnviarMensagemEmail() {
        Plataforma plataforma = new PlataformaEmail();
        plataforma.enviarMensagem("Teste de e-mail");
        // Apenas valida visualmente que "Enviando e-mail" aparece.
        assertTrue(true); // Placeholder para garantir execução sem exceções.
    }

    @Test
    public void testEnviarMensagemSMS() {
        Plataforma plataforma = new PlataformaSMS();
        plataforma.enviarMensagem("Teste de SMS");
        // Apenas valida visualmente que "Enviando SMS" aparece.
        assertTrue(true); // Placeholder para garantir execução sem exceções.
    }
}