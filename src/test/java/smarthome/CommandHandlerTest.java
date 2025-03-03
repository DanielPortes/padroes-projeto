package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.command.*;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandHandlerTest {
    private HomeCentral central;
    private DeviceFactory philipsFactory;
    private DeviceFactory samsungFactory;
    private Room livingRoom;
    private Room bedroom;
    private LightDevice livingRoomLight;
    private ThermostatDevice bedroomThermostat;

    @BeforeEach
    public void setup() {
        central = HomeCentral.getInstance();
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");
        samsungFactory = DeviceFactoryProducer.getFactory("SAMSUNG");

        // Criar cômodos
        livingRoom = central.getRoomManager().createRoom("Test Living Room", "Living Room");
        bedroom = central.getRoomManager().createRoom("Test Bedroom", "Bedroom");

        // Criar dispositivos
        livingRoomLight = philipsFactory.createLight("Test Living Room Light");
        bedroomThermostat = samsungFactory.createThermostat("Test Bedroom Thermostat");

        // Adicionar dispositivos aos cômodos
        livingRoom.addDevice(livingRoomLight);
        bedroom.addDevice(bedroomThermostat);

        // Registrar dispositivos
        central.getDeviceManager().registerDevice(livingRoomLight);
        central.getDeviceManager().registerDevice(bedroomThermostat);
    }

    @Test
    public void testDeviceCommandHandler() {
        DeviceCommandHandler handler = new DeviceCommandHandler();

        // Teste com dispositivo existente
        Command onCommand = new Command("DEVICE", "Test Living Room Light")
                .addParameter("action", "ON");
        String result = handler.handleCommand(onCommand);
        assertTrue(result.contains("turned on"), "Device should turn on");
        assertTrue(livingRoomLight.isActive(), "Light should be active");

        // Teste com dispositivo inexistente
        Command invalidCommand = new Command("DEVICE", "Nonexistent Device")
                .addParameter("action", "ON");
        result = handler.handleCommand(invalidCommand);
        assertTrue(result.contains("not found"), "Should report device not found");

        // Teste sem parâmetro de ação
        Command noActionCommand = new Command("DEVICE", "Test Living Room Light");
        result = handler.handleCommand(noActionCommand);
        assertTrue(result.contains("Missing action"), "Should require action parameter");

        // Teste passando para o próximo manipulador
        Command roomCommand = new Command("ROOM", "Test Living Room");
        handler.setNext(new RoomCommandHandler());
        result = handler.handleCommand(roomCommand);
        assertFalse(result.contains("End of chain"), "Command should be passed to next handler");
    }

    @Test
    public void testRoomCommandHandler() {
        RoomCommandHandler handler = new RoomCommandHandler();

        // Teste com cômodo existente - ligar
        Command onCommand = new Command("ROOM", "Test Living Room")
                .addParameter("action", "ON");
        String result = handler.handleCommand(onCommand);
        assertTrue(livingRoomLight.isActive(), "All devices in room should be on");

        // Teste com cômodo existente - desligar
        Command offCommand = new Command("ROOM", "Test Living Room")
                .addParameter("action", "OFF");
        result = handler.handleCommand(offCommand);
        assertFalse(livingRoomLight.isActive(), "All devices in room should be off");

        // Teste com cômodo inexistente
        Command invalidCommand = new Command("ROOM", "Nonexistent Room")
                .addParameter("action", "ON");
        result = handler.handleCommand(invalidCommand);
        assertTrue(result.contains("not found"), "Should report room not found");

        // Teste sem parâmetro de ação
        Command noActionCommand = new Command("ROOM", "Test Living Room");
        result = handler.handleCommand(noActionCommand);
        assertTrue(result.contains("Missing action"), "Should require action parameter");

        // Teste com ação inválida
        Command invalidActionCommand = new Command("ROOM", "Test Living Room")
                .addParameter("action", "INVALID");
        result = handler.handleCommand(invalidActionCommand);
        assertTrue(result.contains("Unknown room action"), "Should report unknown action");
    }

    @Test
    public void testSecurityCommandHandler() {
        SecurityCommandHandler handler = new SecurityCommandHandler();

        // Teste armar sistema
        Command armCommand = new Command("SECURITY", "System")
                .addParameter("action", "ARM");
        String result = handler.handleCommand(armCommand);
        assertTrue(result.contains("armed"), "Security system should be armed");
        assertTrue(central.getSecuritySystem().isArmed(), "Security system should be armed");

        // Teste desarmar sistema sem código
        Command disarmNoCodeCommand = new Command("SECURITY", "System")
                .addParameter("action", "DISARM");
        result = handler.handleCommand(disarmNoCodeCommand);
        assertTrue(result.contains("code required"), "Should require security code");
        assertTrue(central.getSecuritySystem().isArmed(), "Security system should remain armed");

        // Teste desarmar sistema com código inválido
        Command disarmInvalidCodeCommand = new Command("SECURITY", "System")
                .addParameter("action", "DISARM")
                .addParameter("code", "12345");
        result = handler.handleCommand(disarmInvalidCodeCommand);
        assertTrue(result.contains("Invalid security code"), "Should report invalid code");
        assertTrue(central.getSecuritySystem().isArmed(), "Security system should remain armed");

        // Teste com ação inválida
        Command invalidActionCommand = new Command("SECURITY", "System")
                .addParameter("action", "INVALID");
        result = handler.handleCommand(invalidActionCommand);
        assertTrue(result.contains("Unknown security action"), "Should report unknown action");

        // Teste sem parâmetro de ação
        Command noActionCommand = new Command("SECURITY", "System");
        result = handler.handleCommand(noActionCommand);
        assertTrue(result.contains("Missing action"), "Should require action parameter");
    }

    @Test
    public void testCommandProcessor() {
        CommandProcessor processor = new CommandProcessor();

        // Teste com comando de dispositivo
        Command deviceCommand = new Command("DEVICE", "Test Living Room Light")
                .addParameter("action", "ON");
        String result = processor.processCommand(deviceCommand);
        assertTrue(result.contains("turned on"), "Device command should be processed");
        assertTrue(livingRoomLight.isActive(), "Light should be on");

        // Teste com comando de cômodo
        Command roomCommand = new Command("ROOM", "Test Living Room")
                .addParameter("action", "OFF");
        result = processor.processCommand(roomCommand);
        assertTrue(result.contains("turned off"), "Room command should be processed");
        assertFalse(livingRoomLight.isActive(), "Light should be off");

        // Teste com comando de segurança
        Command securityCommand = new Command("SECURITY", "System")
                .addParameter("action", "ARM");
        result = processor.processCommand(securityCommand);
        assertTrue(result.contains("armed"), "Security command should be processed");
        assertTrue(central.getSecuritySystem().isArmed(), "Security system should be armed");

        // Teste com comando desconhecido
        Command unknownCommand = new Command("UNKNOWN", "Target");
        result = processor.processCommand(unknownCommand);
        assertTrue(result.contains("not handled"), "Unknown command should reach end of chain");

        // Teste com sobrecarga de processCommand
        result = processor.processCommand("DEVICE", "Test Living Room Light",
                java.util.Map.of("action", "ON"));
        assertTrue(result.contains("turned on"), "Command should be processed with map parameters");
    }
}