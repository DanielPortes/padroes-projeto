package smarthome;


import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmartHomeTest {
    private HomeCentral central;
    private DeviceFactory philipsFactory;

    @BeforeEach
    public void setup() {
        central = HomeCentral.getInstance();
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");
    }

    @Test
    public void testSingletonPattern() {
        HomeCentral central1 = HomeCentral.getInstance();
        HomeCentral central2 = HomeCentral.getInstance();

        assertSame(central1, central2, "HomeCentral should be a singleton");
    }

    @Test
    public void testAbstractFactoryPattern() {
        // Cria uma luz com a fábrica Philips
        LightDevice philipsLight = philipsFactory.createLight("Test Light");

        assertEquals("Light", philipsLight.getType(), "Should create correct device type");
        assertEquals("Philips Smart Home", philipsLight.getImplementation().getVendorInfo(),
                "Should create device with correct implementation");

        // Cria uma luz com a fábrica Samsung
        DeviceFactory samsungFactory = DeviceFactoryProducer.getFactory("SAMSUNG");
        LightDevice samsungLight = samsungFactory.createLight("Samsung Light");

        assertEquals("Samsung Smart Things", samsungLight.getImplementation().getVendorInfo(),
                "Should create device with Samsung implementation");
    }

    @Test
    public void testBridgePattern() {
        // Cria dispositivos com implementações diferentes
        LightDevice philipsLight = philipsFactory.createLight("Bridge Test Light");

        // Testa se a abstração e implementação estão separadas corretamente
        assertEquals("Light", philipsLight.getType(), "Device abstraction works");
        assertEquals("Philips Smart Home", philipsLight.getImplementation().getVendorInfo(),
                "Implementation works");

        // Testa operação na implementação
        String operationResult = philipsLight.getImplementation().performOperation("TEST");
        assertEquals("Philips device performing: TEST", operationResult,
                "Bridge should allow operations to be forwarded to implementation");
    }

    @Test
    public void testStatePattern() {
        LightDevice light = philipsFactory.createLight("State Test Light");

        // Estado inicial deve ser OFF
        assertFalse(light.isActive(), "Initial state should be OFF");

        // Mudar para ON
        String result = light.execute("ON");
        assertEquals("Device turned on", result, "Should process ON command");
        assertTrue(light.isActive(), "State should be active after ON command");

        // Mudar para OFF
        result = light.execute("OFF");
        assertEquals("Device turned off", result, "Should process OFF command");
        assertFalse(light.isActive(), "State should be inactive after OFF command");
    }

    @Test
    public void testCommandChainOfResponsibility() {
        // Configura um cômodo e dispositivo para teste
        Room room = central.getRoomManager().createRoom("Test Room", "Bedroom");
        LightDevice light = philipsFactory.createLight("Chain Test Light");
        room.addDevice(light);
        central.getDeviceManager().registerDevice(light);

        // Cria o processador de comandos
        com.smarthome.command.CommandProcessor processor = new com.smarthome.command.CommandProcessor();

        // Testa um comando para dispositivo
        com.smarthome.command.Command deviceCommand = new com.smarthome.command.Command("DEVICE", "Chain Test Light")
                .addParameter("action", "ON");
        String result = processor.processCommand(deviceCommand);
        assertTrue(result.contains("turned on"), "Device command should be processed");
        assertTrue(light.isActive(), "Device should be turned on");

        // Testa um comando para cômodo
        com.smarthome.command.Command roomCommand = new com.smarthome.command.Command("ROOM", "Test Room")
                .addParameter("action", "OFF");
        result = processor.processCommand(roomCommand);
        assertTrue(result.contains("turned off"), "Room command should be processed");
        assertFalse(light.isActive(), "Device should be turned off via room command");
    }

    @Test
    public void testStrategyPattern() {
        // Configura dispositivos para testar as estratégias
        Room room = central.getRoomManager().createRoom("Strategy Test Room", "Living Room");
        LightDevice light = philipsFactory.createLight("Strategy Test Light");
        room.addDevice(light);
        central.getDeviceManager().registerDevice(light);

        // Cria o gerenciador de energia
        com.smarthome.strategy.PowerManager powerManager = new com.smarthome.strategy.PowerManager();

        // Testa estratégia de máxima economia
        powerManager.setStrategy("Maximum Power Saving");
        String result = powerManager.applyStrategy(light);
        assertTrue(result.contains("Max Economy"), "Should apply correct strategy");
        assertFalse(light.isActive(), "Light should be off in max economy mode");

        // Testa estratégia de conforto
        powerManager.setStrategy("Comfort Mode");
        result = powerManager.applyStrategy(light);
        assertTrue(result.contains("Comfort"), "Should apply correct strategy");
        assertTrue(light.isActive(), "Light should be on in comfort mode");
    }

    @Test
    public void testObserverPattern() {
        // Cria objeto para capturar as notificações
        final StringBuilder notificationLog = new StringBuilder();

        // Adiciona observer ao centro de notificações
        central.getNotificationCenter().addObserver(event ->
                notificationLog.append("Event: ").append(event.getType())
                        .append(" - ").append(event.getDescription())
                        .append("\n")
        );

        // Envia notificação
        central.getNotificationCenter().sendNotification("Test notification");

        // Verifica se o observer recebeu a notificação
        assertTrue(notificationLog.toString().contains("Test notification"),
                "Observer should receive notification");
    }

    @Test
    public void testDecoratorPattern() {
        // Cria dispositivo básico
        LightDevice basicLight = philipsFactory.createLight("Basic Light");

        // Aplica decorador
        AbstractDevice secureLight = new com.smarthome.decorators.SecurityEnabledDecorator(basicLight);
        AbstractDevice monitoredSecureLight = new com.smarthome.decorators.EnergyMonitoringDecorator(secureLight);

        // Testa que o tipo e nome são preservados
        assertEquals("Light", monitoredSecureLight.getType(), "Decorator should preserve type");
        assertEquals("Basic Light", monitoredSecureLight.getName(), "Decorator should preserve name");

        // Testa que o status contém informações adicionadas pelo decorador
        String status = monitoredSecureLight.getStatus();
        assertTrue(status.contains("Security"), "Status should include security info");
        assertTrue(status.contains("Power Usage"), "Status should include power usage info");
    }

    @Test
    public void testTemplateMethodPattern() {
        // Cria uma rotina personalizada para teste
        com.smarthome.automation.AutomationRoutine routine = new com.smarthome.automation.AutomationRoutine("Test Routine") {
            @Override
            protected String executeRoutineSteps() {
                return "Custom routine steps executed";
            }
        };

        // Executa a rotina
        String result = routine.execute();

        assertTrue(result.contains("Starting routine"), "Template method should include pre-processing");
        assertTrue(result.contains("Custom routine steps"), "Template method should execute concrete steps");
        assertTrue(result.contains("completed successfully"), "Template method should include post-processing");
    }

    @Test
    public void testMediatorPattern() {
        // Cria mediador
        com.smarthome.mediator.SmartHomeMediator mediator = new com.smarthome.mediator.CentralMediator();

        // Cria subsistemas
        com.smarthome.mediator.PowerController powerController = new com.smarthome.mediator.PowerController(mediator);
        com.smarthome.mediator.PresenceController presenceController = new com.smarthome.mediator.PresenceController(mediator);

        // Testa comunicação via mediador
        presenceController.userLeft();

        // Testa isSomeoneHome
        assertFalse(presenceController.isSomeoneHome(), "Presence status should change after userLeft");
    }

    @Test
    public void testFlyweightPattern() {
        // Cria vários cômodos do mesmo tipo
        Room room1 = central.getRoomManager().createRoom("Room1", "Living Room");
        Room room2 = central.getRoomManager().createRoom("Room2", "Living Room");
        Room room3 = central.getRoomManager().createRoom("Room3", "Living Room");

        // Testa que os atributos são compartilhados (mesma referência)
        assertEquals(room1.getAttributes().getType(), room2.getAttributes().getType(),
                "Rooms should share attributes");
        assertEquals(room1.getAttributes().getDefaultLightLevel(), room3.getAttributes().getDefaultLightLevel(),
                "Rooms should share default settings");
    }
}