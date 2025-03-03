package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.decorators.EnergyMonitored;
import trabalhofinal.smarthome.decorators.EnergyMonitoringDecorator;
import trabalhofinal.smarthome.decorators.SecurityEnabledDecorator;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.visitor.DiagnosticVisitor;
import trabalhofinal.smarthome.visitor.EnergyReportVisitor;
import trabalhofinal.smarthome.visitor.HomeVisitor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class DecoratorsAndVisitorsTest {
    private HomeCentral central;
    private DeviceFactory philipsFactory;
    private Room livingRoom;
    private LightDevice basicLight;
    private ThermostatDevice basicThermostat;

    @BeforeEach
    public void setup() {
        central = HomeCentral.getInstance();
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");

        // Criar cômodo
        livingRoom = central.getRoomManager().createRoom("Visitor Test Room", "Living Room");

        // Criar dispositivos básicos
        basicLight = philipsFactory.createLight("Basic Test Light");
        basicThermostat = philipsFactory.createThermostat("Basic Test Thermostat");

        // Adicionar ao cômodo
        livingRoom.addDevice(basicLight);
        livingRoom.addDevice(basicThermostat);

        // Registrar dispositivos
        central.getDeviceManager().registerDevice(basicLight);
        central.getDeviceManager().registerDevice(basicThermostat);
    }

    @Test
    public void testEnergyMonitoringDecorator() {
        // Aplicar decorator
        AbstractDevice monitoredLight = new EnergyMonitoringDecorator(basicLight);

        // Verificar que tipo e nome são preservados
        assertEquals("Light", monitoredLight.getType(), "Decorator should preserve device type");
        assertEquals("Basic Test Light", monitoredLight.getName(), "Decorator should preserve device name");

        // Verificar comportamento inicial
        EnergyMonitored energyMonitored = (EnergyMonitored) monitoredLight;
        assertEquals(0.0, energyMonitored.getCurrentPowerUsage(), "Initial power usage should be 0");
        assertEquals(0, energyMonitored.getUsageHistory().size(), "Initial usage history should be empty");

        // Ligar dispositivo para testar monitoramento
        monitoredLight.execute("ON");

        // Verificar monitoramento
        assertTrue(energyMonitored.getCurrentPowerUsage() > 0, "Power usage should increase when on");
        assertEquals(1, energyMonitored.getUsageHistory().size(), "Usage history should record state change");

        // Desligar dispositivo
        monitoredLight.execute("OFF");

        // Verificar monitoramento
        assertEquals(0.0, energyMonitored.getCurrentPowerUsage(), "Power usage should be 0 when off");
        assertEquals(2, energyMonitored.getUsageHistory().size(), "Usage history should record another change");

        // Verificar status
        String status = monitoredLight.getStatus();
        assertTrue(status.contains("Current Power Usage:"), "Status should include power usage info");
    }

    @Test
    public void testSecurityEnabledDecorator() {
        // Aplicar decorator
        AbstractDevice secureLight = new SecurityEnabledDecorator(basicLight);

        // Verificar que tipo e nome são preservados
        assertEquals("Light", secureLight.getType(), "Decorator should preserve device type");
        assertEquals("Basic Test Light", secureLight.getName(), "Decorator should preserve device name");

        // Verificar comportamento inicial
        SecurityEnabledDecorator securityDecorator = (SecurityEnabledDecorator) secureLight;
        assertTrue(securityDecorator.isSecurityEnabled(), "Security should be enabled by default");

        // Desativar segurança
        securityDecorator.setSecurityEnabled(false);
        assertFalse(securityDecorator.isSecurityEnabled(), "Security should be disabled after change");

        // Verificar status
        String status = secureLight.getStatus();
        assertTrue(status.contains("Security: Disabled"), "Status should include security info");

        // Reativar segurança
        securityDecorator.setSecurityEnabled(true);

        // Armar sistema de segurança
        central.getSecuritySystem().arm();

        // Criar observer para capturar notificações
        final boolean[] notificationSent = {false};
        central.getNotificationCenter().addObserver(event -> {
            if (event.getDescription().contains("Security alert")) {
                notificationSent[0] = true;
            }
        });

        // Testar execução de comando com segurança ativada
        secureLight.execute("ON");
        assertTrue(notificationSent[0], "Security notification should be sent when armed");
    }

    @Test
    public void testDiagnosticVisitor() {
        // Criar visitor
        DiagnosticVisitor visitor = new DiagnosticVisitor();

        // Ligar um dos dispositivos
        basicLight.execute("ON");

        // Visitar dispositivos
        String lightDiag = visitor.visitLight(basicLight);
        assertTrue(lightDiag.contains("DIAGNOSTIC - Light Device"), "Should generate light diagnostic");
        assertTrue(lightDiag.contains("Status: ON"), "Should report correct light status");

        String thermostatDiag = visitor.visitThermostat(basicThermostat);
        assertTrue(thermostatDiag.contains("DIAGNOSTIC - Thermostat Device"), "Should generate thermostat diagnostic");
        assertTrue(thermostatDiag.contains("Status: OFF"), "Should report correct thermostat status");

        // Visitar cômodo
        String roomDiag = visitor.visitRoom(livingRoom);
        assertTrue(roomDiag.contains("DIAGNOSTIC - Room"), "Should generate room diagnostic");
        assertTrue(roomDiag.contains("Active Devices: 1/2"), "Should report correct active device count");

        // Testar método accept
        String acceptResult = livingRoom.accept(visitor);
        assertEquals(roomDiag, acceptResult, "Accept method should call visitor correctly");

        // Testar resumo
        String summary = visitor.getSummary();
        assertTrue(summary.contains("Total Devices Visited: 2"), "Should count visited devices");
        assertTrue(summary.contains("Active Devices: 1"), "Should count active devices");
        assertTrue(summary.contains("System Activity Level: 50%"), "Should calculate activity level");
    }

    @Test
    public void testEnergyReportVisitor() {
        // Criar visitor
        EnergyReportVisitor visitor = new EnergyReportVisitor();

        // Ligar dispositivos
        basicLight.execute("ON");
        basicThermostat.execute("ON");

        // Criar dispositivo com monitoramento de energia
        AbstractDevice monitoredLight = new EnergyMonitoringDecorator(
                philipsFactory.createLight("Monitored Light"));
        monitoredLight.execute("ON");
        livingRoom.addDevice(monitoredLight);

        // Criar dispositivo de termostato com monitoramento de energia
        AbstractDevice monitoredThermostat = new EnergyMonitoringDecorator(
                philipsFactory.createThermostat("Monitored Thermostat"));
        monitoredThermostat.execute("ON");
        livingRoom.addDevice(monitoredThermostat);

        // Visitar dispositivos
        String lightReport = visitor.visitLight(basicLight);
        assertTrue(lightReport.contains("ENERGY - Light"), "Should generate light energy report");
        assertTrue(lightReport.contains("Status: ON"), "Should report correct light status");
        assertTrue(lightReport.contains("Estimated Power"), "Should report estimated power");

        String monitoredLightReport = visitor.visitLight((LightDevice)monitoredLight);
        assertTrue(monitoredLightReport.contains("Measured Power"), "Should report measured power for monitored device");

        String thermostatReport = visitor.visitThermostat(basicThermostat);
        assertTrue(thermostatReport.contains("ENERGY - Thermostat"), "Should generate thermostat energy report");
        assertTrue(thermostatReport.contains("Estimated Power"), "Should report estimated power");

        String monitoredThermostatReport = visitor.visitThermostat((ThermostatDevice)monitoredThermostat);
        assertTrue(monitoredThermostatReport.contains("Measured Power"), "Should report measured power for monitored thermostat");

        // Visitar cômodo
        String roomReport = visitor.visitRoom(livingRoom);
        assertTrue(roomReport.contains("ENERGY - Room"), "Should generate room energy report");
        assertTrue(roomReport.contains("Total Energy Usage"), "Should report total energy usage");

        // Testar relatório de energia
        String energyReport = visitor.getEnergyReport();
        assertTrue(energyReport.contains("SMART HOME ENERGY REPORT"), "Should generate complete energy report");
        assertTrue(energyReport.contains("Energy Usage by Room"), "Should report energy usage by room");
    }

    @Test
    public void testNestedDecorators() {
        // Aplicar múltiplos decoradores
        AbstractDevice baseDevice = basicLight;
        AbstractDevice secureDevice = new SecurityEnabledDecorator(baseDevice);
        AbstractDevice monitoredSecureDevice = new EnergyMonitoringDecorator(secureDevice);

        // Verificar que tipo e nome são preservados através de decoradores aninhados
        assertEquals("Light", monitoredSecureDevice.getType(), "Nested decorators should preserve type");
        assertEquals("Basic Test Light", monitoredSecureDevice.getName(), "Nested decorators should preserve name");

        // Testar execução de comando através dos decoradores
        monitoredSecureDevice.execute("ON");
        assertTrue(baseDevice.isActive(), "Base device should be activated");

        // Verificar status combinado
        String status = monitoredSecureDevice.getStatus();
        assertTrue(status.contains("Device: Basic Test Light"), "Status should include device name");
        assertTrue(status.contains("Security: Enabled"), "Status should include security info");
        assertTrue(status.contains("Current Power Usage:"), "Status should include power usage info");
    }
}