package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.automation.MorningRoutine;
import trabalhofinal.smarthome.automation.SecurityAlertRoutine;
import trabalhofinal.smarthome.command.Command;
import trabalhofinal.smarthome.command.CommandProcessor;
import trabalhofinal.smarthome.command.DeviceCommandHandler;
import trabalhofinal.smarthome.command.RoomCommandHandler;
import trabalhofinal.smarthome.command.SecurityCommandHandler;
import trabalhofinal.smarthome.core.DeviceManager;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.NotificationCenter;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.core.RoomManager;
import trabalhofinal.smarthome.core.SecuritySystem;
import trabalhofinal.smarthome.decorators.EnergyMonitoringDecorator;
import trabalhofinal.smarthome.decorators.SecurityEnabledDecorator;
import trabalhofinal.smarthome.devices.*;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.flyweight.RoomAttributes;
import trabalhofinal.smarthome.flyweight.RoomAttributesFactory;
import trabalhofinal.smarthome.mediator.CentralMediator;
import trabalhofinal.smarthome.mediator.PowerController;
import trabalhofinal.smarthome.mediator.PresenceController;
import trabalhofinal.smarthome.mediator.SmartHomeMediator;
import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.observer.LoggingObserver;
import trabalhofinal.smarthome.observer.MobileAppNotifier;
import trabalhofinal.smarthome.observer.Observer;
import trabalhofinal.smarthome.states.DeviceState;
import trabalhofinal.smarthome.states.OffState;
import trabalhofinal.smarthome.states.OnState;
import trabalhofinal.smarthome.states.StandbyState;
import trabalhofinal.smarthome.strategy.BalancedPowerStrategy;
import trabalhofinal.smarthome.strategy.ComfortPowerStrategy;
import trabalhofinal.smarthome.strategy.MaxEconomyPowerStrategy;
import trabalhofinal.smarthome.strategy.PowerManager;
import trabalhofinal.smarthome.visitor.DiagnosticVisitor;
import trabalhofinal.smarthome.visitor.EnergyReportVisitor;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceAndStateTest {
    private DeviceFactory philipsFactory;
    private DeviceFactory samsungFactory;

    @BeforeEach
    public void setup() {
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");
        samsungFactory = DeviceFactoryProducer.getFactory("SAMSUNG");
    }

    @Test
    public void testLightDevice() {
        // Teste de dispositivo de luz
        LightDevice philipsLight = philipsFactory.createLight("Test Philips Light");

        // Verificar valores iniciais
        assertEquals("Light", philipsLight.getType(), "Should have correct type");
        assertEquals("Test Philips Light", philipsLight.getName(), "Should have correct name");
        assertEquals(0, philipsLight.getBrightness(), "Initial brightness should be 0");
        assertFalse(philipsLight.isActive(), "Device should be inactive initially");

        // Testar mudança de brilho
        philipsLight.setBrightness(75);
        assertEquals(75, philipsLight.getBrightness(), "Brightness should be updated");

        // Testar execução de comando
        String result = philipsLight.execute("ON");
        assertTrue(philipsLight.isActive(), "Device should be active after ON command");

        // Testar limites de brilho
        philipsLight.setBrightness(150); // Acima do máximo
        assertEquals(100, philipsLight.getBrightness(), "Brightness should be capped at 100");

        philipsLight.setBrightness(-10); // Abaixo do mínimo
        assertEquals(0, philipsLight.getBrightness(), "Brightness should be capped at 0");

        // Testar status
        String status = philipsLight.getStatus();
        assertTrue(status.contains("Device: Test Philips Light"), "Status should include device name");
        assertTrue(status.contains("Type: Light"), "Status should include device type");
        assertTrue(status.contains("Brightness: 0%"), "Status should include brightness");
    }

    @Test
    public void testThermostatDevice() {
        // Teste de dispositivo de termostato
        ThermostatDevice samsungThermostat = samsungFactory.createThermostat("Test Samsung Thermostat");

        // Verificar valores iniciais
        assertEquals("Thermostat", samsungThermostat.getType(), "Should have correct type");
        assertEquals("Test Samsung Thermostat", samsungThermostat.getName(), "Should have correct name");
        assertEquals(21.0, samsungThermostat.getTemperature(), "Initial temperature should be 21.0");
        assertEquals(21.0, samsungThermostat.getTargetTemperature(), "Initial target temperature should be 21.0");
        assertFalse(samsungThermostat.isActive(), "Device should be inactive initially");

        // Testar mudança de temperatura
        samsungThermostat.setTemperature(23.5);
        assertEquals(23.5, samsungThermostat.getTemperature(), "Temperature should be updated");

        samsungThermostat.setTargetTemperature(22.0);
        assertEquals(22.0, samsungThermostat.getTargetTemperature(), "Target temperature should be updated");

        // Testar execução de comando
        String result = samsungThermostat.execute("ON");
        assertTrue(samsungThermostat.isActive(), "Device should be active after ON command");

        // Testar status
        String status = samsungThermostat.getStatus();
        System.out.println("Thermostat status: " + status);

        assertTrue(status.contains("Device: Test Samsung Thermostat"), "Status should include device name");
        assertTrue(status.contains("Type: Thermostat"), "Status should include device type");

        // Verificar a temperatura de forma mais flexível
        double temp = samsungThermostat.getTemperature();
        String tempStr = String.format("%.1f", temp);
        assertTrue(status.contains(tempStr), "Status should include temperature");

        double targetTemp = samsungThermostat.getTargetTemperature();
        String targetTempStr = String.format("%.1f", targetTemp);
        assertTrue(status.contains(targetTempStr), "Status should include target temperature");
    }

    @Test
    public void testDeviceImplementations() {
        // Testar implementação Philips
        DeviceImplementation philipsImpl = new PhilipsImplementation();
        assertEquals("Philips Smart Home", philipsImpl.getVendorInfo(), "Should return correct vendor info");
        assertEquals("Philips device performing: TEST", philipsImpl.performOperation("TEST"),
                "Should perform operation correctly");

        // Testar implementação Samsung
        DeviceImplementation samsungImpl = new SamsungImplementation();
        assertEquals("Samsung Smart Things", samsungImpl.getVendorInfo(), "Should return correct vendor info");
        assertEquals("Samsung device performing: TEST", samsungImpl.performOperation("TEST"),
                "Should perform operation correctly");
    }

    @Test
    public void testDeviceStates() {
        // Criar dispositivo para teste de estados
        LightDevice light = philipsFactory.createLight("State Test Light");

        // Testar estado Off
        DeviceState offState = new OffState(light);
        assertEquals("OFF", offState.getName(), "State name should be OFF");
        assertFalse(offState.isActive(), "OFF state should be inactive");

        // Testar mudança de estado de Off para On
        String result = offState.handleCommand("ON");
        assertTrue(result.contains("turned on"), "Should process ON command");
        assertTrue(light.getState() instanceof OnState, "State should change to ON");

        // Testar comando inválido no estado Off
        offState = new OffState(light); // Reset para testar novamente
        result = offState.handleCommand("SET_TEMP");
        assertTrue(result.contains("cannot process"), "OFF state should reject most commands");

        // Testar estado On
        DeviceState onState = new OnState(light);
        assertEquals("ON", onState.getName(), "State name should be ON");
        assertTrue(onState.isActive(), "ON state should be active");

        // Testar mudanças de estado a partir do estado On
        result = onState.handleCommand("OFF");
        assertTrue(result.contains("turned off"), "Should process OFF command");
        assertTrue(light.getState() instanceof OffState, "State should change to OFF");

        onState = new OnState(light); // Reset
        result = onState.handleCommand("STANDBY");
        assertTrue(result.contains("standby"), "Should process STANDBY command");
        assertTrue(light.getState() instanceof StandbyState, "State should change to STANDBY");

        // Testar comando inválido no estado On
        onState = new OnState(light); // Reset
        result = onState.handleCommand("ON");
        assertTrue(result.contains("already on"), "ON state should handle redundant ON command");

        // Testar estado Standby
        DeviceState standbyState = new StandbyState(light);
        assertEquals("STANDBY", standbyState.getName(), "State name should be STANDBY");
        assertTrue(standbyState.isActive(), "STANDBY state should be active");

        // Testar mudanças de estado a partir do estado Standby
        result = standbyState.handleCommand("ON");
        assertTrue(result.contains("turned on from standby"), "Should process ON command from standby");
        assertTrue(light.getState() instanceof OnState, "State should change to ON");

        standbyState = new StandbyState(light); // Reset
        result = standbyState.handleCommand("OFF");
        assertTrue(result.contains("turned off from standby"), "Should process OFF command from standby");
        assertTrue(light.getState() instanceof OffState, "State should change to OFF");

        // Testar comando inválido no estado Standby
        standbyState = new StandbyState(light); // Reset
        result = standbyState.handleCommand("UNKNOWN");
        assertTrue(result.contains("standby mode"), "STANDBY state should handle unknown commands");
    }
}



