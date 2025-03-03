package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.devices.DeviceImplementation;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.PhilipsImplementation;
import trabalhofinal.smarthome.devices.SamsungImplementation;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.factories.PhilipsDeviceFactory;
import trabalhofinal.smarthome.factories.SamsungDeviceFactory;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {

    @Test
    public void testDeviceFactoryProducer() {
        // Obter fábricas para cada marca
        DeviceFactory philipsFactory = DeviceFactoryProducer.getFactory("Philips");
        DeviceFactory samsungFactory = DeviceFactoryProducer.getFactory("Samsung");

        // Verificar tipo correto de fábrica
        assertTrue(philipsFactory instanceof PhilipsDeviceFactory, "Should create Philips factory");
        assertTrue(samsungFactory instanceof SamsungDeviceFactory, "Should create Samsung factory");

        // Verificar que fábrica utiliza implementação correta
        LightDevice philipsLight = philipsFactory.createLight("Test Light");
        assertTrue(philipsLight.getImplementation() instanceof PhilipsImplementation,
                "Should use Philips implementation");

        LightDevice samsungLight = samsungFactory.createLight("Test Light");
        assertTrue(samsungLight.getImplementation() instanceof SamsungImplementation,
                "Should use Samsung implementation");

        // Testar com marcas em letras minúsculas
        DeviceFactory philipsAgain = DeviceFactoryProducer.getFactory("philips");
        assertTrue(philipsAgain instanceof PhilipsDeviceFactory, "Should be case insensitive");

        // Testar marca inexistente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DeviceFactoryProducer.getFactory("Unknown");
        });
        assertTrue(exception.getMessage().contains("Unsupported brand"),
                "Should throw exception for unknown brand");
    }

    @Test
    public void testPhilipsDeviceFactory() {
        // Obter fábrica Philips
        DeviceFactory factory = new PhilipsDeviceFactory();

        // Criar luz
        LightDevice light = factory.createLight("Philips Test Light");

        // Verificar propriedades
        assertEquals("Light", light.getType(), "Should create light device");
        assertEquals("Philips Test Light", light.getName(), "Should have correct name");
        assertTrue(light.getImplementation() instanceof PhilipsImplementation,
                "Should use Philips implementation");

        // Criar termostato
        ThermostatDevice thermostat = factory.createThermostat("Philips Test Thermostat");

        // Verificar propriedades
        assertEquals("Thermostat", thermostat.getType(), "Should create thermostat device");
        assertEquals("Philips Test Thermostat", thermostat.getName(), "Should have correct name");
        assertTrue(thermostat.getImplementation() instanceof PhilipsImplementation,
                "Should use Philips implementation");
    }

    @Test
    public void testSamsungDeviceFactory() {
        // Obter fábrica Samsung
        DeviceFactory factory = new SamsungDeviceFactory();

        // Criar luz
        LightDevice light = factory.createLight("Samsung Test Light");

        // Verificar propriedades
        assertEquals("Light", light.getType(), "Should create light device");
        assertEquals("Samsung Test Light", light.getName(), "Should have correct name");
        assertTrue(light.getImplementation() instanceof SamsungImplementation,
                "Should use Samsung implementation");

        // Criar termostato
        ThermostatDevice thermostat = factory.createThermostat("Samsung Test Thermostat");

        // Verificar propriedades
        assertEquals("Thermostat", thermostat.getType(), "Should create thermostat device");
        assertEquals("Samsung Test Thermostat", thermostat.getName(), "Should have correct name");
        assertTrue(thermostat.getImplementation() instanceof SamsungImplementation,
                "Should use Samsung implementation");
    }

    @Test
    public void testImplementations() {
        // Testar implementação Philips
        DeviceImplementation philipsImpl = new PhilipsImplementation();
        assertEquals("Philips Smart Home", philipsImpl.getVendorInfo(), "Should return correct vendor info");
        assertTrue(philipsImpl.performOperation("TEST").contains("Philips device"),
                "Should identify as Philips device");

        // Testar implementação Samsung
        DeviceImplementation samsungImpl = new SamsungImplementation();
        assertEquals("Samsung Smart Things", samsungImpl.getVendorInfo(), "Should return correct vendor info");
        assertTrue(samsungImpl.performOperation("TEST").contains("Samsung device"),
                "Should identify as Samsung device");
    }
}