package padroescriacao.exercicio01;

import org.junit.Test;

import static org.junit.Assert.*;

public class FabricaMensagemTest {

    @Test
    public void testCriarMensagemCurta() {
        FabricaMensagem fabrica = new FabricaMensagemCurta();
        Plataforma plataforma = new PlataformaEmail();
        Mensagem mensagem = fabrica.criarMensagem(plataforma);

        assertNotNull(mensagem);
        assertTrue(mensagem instanceof MensagemCurta);
    }

    @Test
    public void testCriarMensagemLonga() {
        FabricaMensagem fabrica = new FabricaMensagemLonga();
        Plataforma plataforma = new PlataformaSMS();
        Mensagem mensagem = fabrica.criarMensagem(plataforma);

        assertNotNull(mensagem);
        assertTrue(mensagem instanceof MensagemLonga);
    }
}
