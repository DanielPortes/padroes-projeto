package smarthome;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.core.DeviceManager;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.NotificationCenter;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.core.RoomManager;
import trabalhofinal.smarthome.core.SecuritySystem;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.observer.Observer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoreComponentsTest {
    private HomeCentral central;
    private DeviceFactory philipsFactory;

    @BeforeEach
    public void setup() {
        central = HomeCentral.getInstance();
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");
    }

    @Test
    public void testDeviceManager() {
        DeviceManager deviceManager = central.getDeviceManager();

        // Limpar dispositivos existentes
        for (AbstractDevice device : deviceManager.getAllDevices()) {
            deviceManager.unregisterDevice(device.getId());
        }

        // Criar e registrar dispositivos
        LightDevice light1 = philipsFactory.createLight("Test Light 1");
        LightDevice light2 = philipsFactory.createLight("Test Light 2");
        ThermostatDevice thermostat = philipsFactory.createThermostat("Test Thermostat");

        deviceManager.registerDevice(light1);
        deviceManager.registerDevice(light2);
        deviceManager.registerDevice(thermostat);

        // Testar getDeviceById
        Optional<AbstractDevice> retrievedDevice = deviceManager.getDeviceById(light1.getId());
        assertTrue(retrievedDevice.isPresent(), "Should retrieve device by ID");
        assertEquals("Test Light 1", retrievedDevice.get().getName(), "Should retrieve correct device");

        // Testar getDeviceByName
        retrievedDevice = deviceManager.getDeviceByName("Test Light 2");
        assertTrue(retrievedDevice.isPresent(), "Should retrieve device by name");
        assertEquals(light2.getId(), retrievedDevice.get().getId(), "Should retrieve correct device");

        // Testar getAllDevices
        List<AbstractDevice> allDevices = deviceManager.getAllDevices();
        assertEquals(3, allDevices.size(), "Should retrieve all registered devices");

        // Testar getDevicesByType
        List<AbstractDevice> lights = deviceManager.getDevicesByType("Light");
        assertEquals(2, lights.size(), "Should retrieve devices by type");

        // Testar getActiveDevices (primeiro ativar um dispositivo)
        light1.execute("ON");
        List<AbstractDevice> activeDevices = deviceManager.getActiveDevices();
        assertEquals(1, activeDevices.size(), "Should retrieve active devices");

        // Testar getActiveDeviceCount
        assertEquals(1, deviceManager.getActiveDeviceCount(), "Should count active devices");

        // Testar executeCommandOnAllDevices
        String result = deviceManager.executeCommandOnAllDevices("ON");
        assertTrue(light1.isActive(), "All devices should be active");
        assertTrue(light2.isActive(), "All devices should be active");
        assertTrue(thermostat.isActive(), "All devices should be active");

        // Testar unregisterDevice
        deviceManager.unregisterDevice(light1.getId());
        retrievedDevice = deviceManager.getDeviceById(light1.getId());
        assertFalse(retrievedDevice.isPresent(), "Device should be unregistered");
        assertEquals(2, deviceManager.getAllDevices().size(), "Should have one less device");
    }

    @Test
    public void testRoomManager() {
        RoomManager roomManager = central.getRoomManager();

        // Criar cômodos
        Room livingRoom = roomManager.createRoom("Test Living Room", "Living Room");
        Room bedroom = roomManager.createRoom("Test Bedroom", "Bedroom");

        // Testar getRoomById
        Optional<Room> retrievedRoom = roomManager.getRoomById(livingRoom.getId());
        assertTrue(retrievedRoom.isPresent(), "Should retrieve room by ID");
        assertEquals("Test Living Room", retrievedRoom.get().getName(), "Should retrieve correct room");

        // Testar getRoomByName - verificar pelo nome em vez do ID
        retrievedRoom = roomManager.getRoomByName("Test Bedroom");
        assertTrue(retrievedRoom.isPresent(), "Should retrieve room by name");
        assertEquals("Test Bedroom", retrievedRoom.get().getName(), "Should retrieve room with correct name");

        // Testar getAllRooms
        List<Room> allRooms = roomManager.getAllRooms();
        assertTrue(allRooms.size() >= 2, "Should retrieve all rooms");

        // Testar getRoomsByType
        List<Room> livingRooms = roomManager.getRoomsByType("Living Room");
        assertTrue(livingRooms.size() >= 1, "Should retrieve rooms by type");

        // Testar getRoomCount
        int initialCount = roomManager.getRoomCount();
        assertTrue(initialCount >= 2, "Should count all rooms");

        // Testar removeRoom
        roomManager.removeRoom(livingRoom.getId());
        retrievedRoom = roomManager.getRoomById(livingRoom.getId());
        assertFalse(retrievedRoom.isPresent(), "Room should be removed");
        assertEquals(initialCount - 1, roomManager.getRoomCount(), "Room count should decrease");
    }

    @Test
    public void testNotificationCenter() {
        NotificationCenter notificationCenter = central.getNotificationCenter();

        // Criar observer para testar
        AtomicBoolean notificationReceived = new AtomicBoolean(false);
        AtomicBoolean alertReceived = new AtomicBoolean(false);

        Observer<HomeEvent> testObserver = new Observer<HomeEvent>() {
            @Override
            public void update(HomeEvent event) {
                if ("Notification".equals(event.getType())) {
                    notificationReceived.set(true);
                } else if ("Alert".equals(event.getType())) {
                    alertReceived.set(true);
                }
            }
        };

        // Registrar observer
        notificationCenter.addObserver(testObserver);

        // Testar envio de notificação
        notificationCenter.sendNotification("Test notification");
        assertTrue(notificationReceived.get(), "Observer should receive notification");

        // Testar envio de alerta
        notificationCenter.sendAlert("Test Source", "Test alert message");
        assertTrue(alertReceived.get(), "Observer should receive alert");

        // Testar histórico de eventos
        List<HomeEvent> recentEvents = notificationCenter.getRecentEvents(5);
        assertTrue(recentEvents.size() >= 2, "Should record event history");

        // Testar remoção de observer
        notificationCenter.removeObserver(testObserver);

        // Resetar flags
        notificationReceived.set(false);
        alertReceived.set(false);

        // Enviar nova notificação após remover observer
        notificationCenter.sendNotification("Another notification");
        assertFalse(notificationReceived.get(), "Removed observer should not receive notification");
    }

    @Test
    public void testSecuritySystem() {
        SecuritySystem securitySystem = central.getSecuritySystem();

        // Testar armar sistema
        securitySystem.arm();
        assertTrue(securitySystem.isArmed(), "Security system should be armed");
        assertNotNull(securitySystem.getLastArmedTime(), "Should record arm time");

        // Testar desarmar com código inválido
        boolean disarmResult = securitySystem.disarm("invalid-code");
        assertFalse(disarmResult, "Should not disarm with invalid code");
        assertTrue(securitySystem.isArmed(), "Security system should remain armed");

        // Testar trigger de alarme
        AtomicBoolean alarmTriggered = new AtomicBoolean(false);
        Observer<HomeEvent> alarmObserver = event -> {
            if ("SECURITY_BREACH".equals(event.getType())) {
                alarmTriggered.set(true);
            }
        };

        central.getNotificationCenter().addObserver(alarmObserver);
        securitySystem.triggerAlarm("Test", "Security test");
        assertTrue(alarmTriggered.get(), "Should trigger alarm when armed");

        // Limpar observer após o teste
        central.getNotificationCenter().removeObserver(alarmObserver);
    }

    @Test
    public void testRoom() {
        // Criar cômodo
        Room room = central.getRoomManager().createRoom("Test Room", "Living Room");

        // Adicionar dispositivos
        LightDevice light1 = philipsFactory.createLight("Room Test Light 1");
        LightDevice light2 = philipsFactory.createLight("Room Test Light 2");
        ThermostatDevice thermostat = philipsFactory.createThermostat("Room Test Thermostat");

        room.addDevice(light1);
        room.addDevice(light2);
        room.addDevice(thermostat);

        // Testar getDevices
        assertEquals(3, room.getDevices().size(), "Room should have all added devices");

        // Testar getDeviceByName
        Optional<AbstractDevice> retrievedDevice = room.getDeviceByName("Room Test Light 1");
        assertTrue(retrievedDevice.isPresent(), "Should find device by name");
        assertEquals(light1.getId(), retrievedDevice.get().getId(), "Should retrieve correct device");

        // Testar turnAllDevicesOn
        String result = room.turnAllDevicesOn();
        assertTrue(light1.isActive(), "All devices should be turned on");
        assertTrue(light2.isActive(), "All devices should be turned on");
        assertTrue(thermostat.isActive(), "All devices should be turned on");

        // Testar getActiveDeviceCount
        assertEquals(3, room.getActiveDeviceCount(), "Should count active devices");

        // Testar turnAllDevicesOff
        result = room.turnAllDevicesOff();
        assertFalse(light1.isActive(), "All devices should be turned off");
        assertFalse(light2.isActive(), "All devices should be turned off");
        assertFalse(thermostat.isActive(), "All devices should be turned off");

        // Testar removeDevice
        room.removeDevice(light1);
        assertEquals(2, room.getDevices().size(), "Room should have one less device");
        retrievedDevice = room.getDeviceByName("Room Test Light 1");
        assertFalse(retrievedDevice.isPresent(), "Device should be removed from room");

        // Testar getRoomStatus
        String status = room.getRoomStatus();
        assertTrue(status.contains("Room: Test Room"), "Status should include room name");
        assertTrue(status.contains("Active Devices: 0/2"), "Status should show device count");
    }
}