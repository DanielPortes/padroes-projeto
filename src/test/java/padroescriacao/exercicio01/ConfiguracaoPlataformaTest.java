package padroescriacao.exercicio01;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfiguracaoPlataformaTest {

    @Test
    public void testUnicaInstancia() {
        ConfiguracaoPlataforma instancia1 = ConfiguracaoPlataforma.getInstancia();
        ConfiguracaoPlataforma instancia2 = ConfiguracaoPlataforma.getInstancia();

        assertSame(instancia1, instancia2);
    }

    @Test
    public void testAlterarPlataforma() {
        ConfiguracaoPlataforma configuracao = ConfiguracaoPlataforma.getInstancia();
        configuracao.setPlataforma(new PlataformaSMS());

        assertTrue(configuracao.getPlataforma() instanceof PlataformaSMS);
    }

    @Test
    public void testPlataformaPadrao() {
        ConfiguracaoPlataforma configuracao = ConfiguracaoPlataforma.getInstancia();

        assertTrue(configuracao.getPlataforma() instanceof PlataformaEmail);
    }
}
