package smarthome;

import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.SmartHomeDemo;

import static org.junit.jupiter.api.Assertions.*;

public class SmartHomeDemoTest {

    @Test
    public void testRunDemo() {
        // Executar demonstração
        String result = SmartHomeDemo.runDemo();

        // Verificar que a demonstração executou adequadamente
        assertNotNull(result, "Demo should return a result");
        assertTrue(result.length() > 0, "Demo result should not be empty");

        // Verificar etapas principais do demo
        assertTrue(result.contains("Iniciando demonstração"), "Should contain initialization message");
        assertTrue(result.contains("Configurando cômodos"), "Should configure rooms");
        assertTrue(result.contains("Configurando dispositivos"), "Should configure devices");
        assertTrue(result.contains("Configurando rotinas automáticas"), "Should configure routines");

        // Verificar execução de comandos
        assertTrue(result.contains("Processando comandos via Chain of Responsibility"),
                "Should process commands");

        // Verificar ativação de segurança
        assertTrue(result.contains("Ativando sistema de segurança"), "Should activate security");

        // Verificar execução de rotina
        assertTrue(result.contains("Executando rotina matinal"), "Should execute routine");

        // Verificar simulação de alerta
        assertTrue(result.contains("Simulando alerta de segurança"), "Should simulate security alert");

        // Verificar status
        assertTrue(result.contains("STATUS DO SISTEMA"), "Should include system status");
        assertTrue(result.contains("STATUS DOS CÔMODOS"), "Should include room status");

        // Verificar conclusão
        assertTrue(result.contains("Demonstração concluída com sucesso"),
                "Should complete successfully");
    }
}