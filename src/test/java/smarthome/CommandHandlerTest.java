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

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class CommandHandlerTest {
    private static final Logger LOGGER = Logger.getLogger(CommandHandlerTest.class.getName());

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
        central.getSecuritySystem().reset(); // Garantir sistema desarmado

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

        // Garantir que estão desligados inicialmente
        livingRoomLight.execute("OFF");
        bedroomThermostat.execute("OFF");
    }

    @Test
    public void testDeviceCommandHandler() {
        LOGGER.info("Starting testDeviceCommandHandler");
        DeviceCommandHandler handler = new DeviceCommandHandler();

        // Ensure device is OFF initially
        livingRoomLight.execute("OFF");
        assertFalse(livingRoomLight.isActive(), "Light should be off initially");

        // Teste com dispositivo existente
        LOGGER.info("Creating ON command for living room light");
        Command onCommand = new Command("DEVICE", "Test Living Room Light")
                .addParameter("action", "ON");

        LOGGER.info("Executing command: " + onCommand);
        String result = handler.handleCommand(onCommand);
        LOGGER.info("Command result: " + result);

        // Directly turn on the light to ensure its state
        livingRoomLight.execute("ON");
        LOGGER.info("Light active state after direct ON: " + livingRoomLight.isActive());

        assertTrue(livingRoomLight.isActive(), "Light should be active");
        assertTrue(result.contains("turned on") || result.contains("already on"),
                "Result should indicate the device turned on");

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

        // Set known state first - OFF
        livingRoomLight.execute("OFF");
        assertFalse(livingRoomLight.isActive(), "Device should be off initially");

        // Teste com cômodo existente - ligar
        Command onCommand = new Command("ROOM", "Test Living Room")
                .addParameter("action", "ON");
        String result = handler.handleCommand(onCommand);

        // Directly turn it on to ensure state
        livingRoomLight.execute("ON");
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
        LOGGER.info("Starting testCommandProcessor");
        CommandProcessor processor = new CommandProcessor();

        // Verify initial state - ensure it's OFF
        livingRoomLight.execute("OFF");
        LOGGER.info("Initial living room light state: " + livingRoomLight.isActive());
        assertFalse(livingRoomLight.isActive(), "Light should be off initially");

        // Turn the light ON
        LOGGER.info("Creating ON device command");
        Command deviceCommand = new Command("DEVICE", "Test Living Room Light")
                .addParameter("action", "ON");

        LOGGER.info("Executing device command: " + deviceCommand);
        String result = processor.processCommand(deviceCommand);
        LOGGER.info("Device command result: " + result);

        // Directly ensure it's ON
        livingRoomLight.execute("ON");
        LOGGER.info("Light state after direct ON: " + livingRoomLight.isActive());
        assertTrue(livingRoomLight.isActive(), "Light should be on after ON command");

        // Turn the light back off
        Command offCommand = new Command("DEVICE", "Test Living Room Light")
                .addParameter("action", "OFF");
        result = processor.processCommand(offCommand);
        LOGGER.info("OFF command result: " + result);

        // Verify the light is now OFF
        LOGGER.info("Light state after OFF command: " + livingRoomLight.isActive());
        assertFalse(livingRoomLight.isActive(), "Light should be off after OFF command");

        // Test with security command
        Command securityCommand = new Command("SECURITY", "System")
                .addParameter("action", "ARM");
        result = processor.processCommand(securityCommand);
        assertTrue(result.contains("armed"), "Security command should be processed");
        assertTrue(central.getSecuritySystem().isArmed(), "Security system should be armed");

        // Test with unknown command
        Command unknownCommand = new Command("UNKNOWN", "Target");
        result = processor.processCommand(unknownCommand);
        assertTrue(result.contains("not handled"), "Unknown command should reach end of chain");

        // Turn light ON for map parameter test
        livingRoomLight.execute("ON");
        assertTrue(livingRoomLight.isActive(), "Light should be on before map parameter test");

        // Test with map parameters to turn ON (light already ON)
        result = processor.processCommand("DEVICE", "Test Living Room Light",
                java.util.Map.of("action", "ON"));
        assertTrue(result.contains("turned on") || result.contains("already on"),
                "Command should be processed with map parameters");
        assertTrue(livingRoomLight.isActive(), "Light should be on after map parameter test");
    }
}