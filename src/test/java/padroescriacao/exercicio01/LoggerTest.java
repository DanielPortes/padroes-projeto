package padroescriacao.exercicio01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import padroescriacao.exercicio01.singleton.Logger;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {
    private Logger logger;

    @BeforeEach
    public void resetLogger() throws Exception {
        logger = Logger.getInstance();

        Field logsField = Logger.class.getDeclaredField("logs");
        logsField.setAccessible(true);
        ((List<?>) logsField.get(logger)).clear();
    }

    @Test
    public void testSingletonInstance() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        assertSame(logger1, logger2, "A instância do Logger deve ser única.");
    }

    @Test
    public void testLogMessage() {
        Logger logger = Logger.getInstance();
        logger.log("Teste de Log");

        assertTrue(logger.getLogs().contains("[LOG]: Teste de Log"), "A mensagem de log deve ser armazenada corretamente.");
    }

    @Test
    public void testMultipleLogMessages() {
        Logger logger = Logger.getInstance();
        logger.log("Primeira mensagem");
        logger.log("Segunda mensagem");

        assertEquals(2, logger.getLogs().size(), "Deve armazenar exatamente 2 mensagens de log.");
    }

    @Test
    public void testLogNullMessage() {
        logger.log(null);

        assertTrue(logger.getLogs().contains("[LOG]: null"),
                "O Logger deve lidar com mensagens nulas corretamente.");
    }

}
