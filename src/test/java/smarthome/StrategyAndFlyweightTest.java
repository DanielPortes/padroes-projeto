package smarthome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.flyweight.RoomAttributes;
import trabalhofinal.smarthome.flyweight.RoomAttributesFactory;
import trabalhofinal.smarthome.strategy.BalancedPowerStrategy;
import trabalhofinal.smarthome.strategy.ComfortPowerStrategy;
import trabalhofinal.smarthome.strategy.MaxEconomyPowerStrategy;
import trabalhofinal.smarthome.strategy.PowerManager;
import trabalhofinal.smarthome.strategy.PowerSavingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyAndFlyweightTest {
    private HomeCentral central;
    private DeviceFactory philipsFactory;
    private List<AbstractDevice> devices;

    @BeforeEach
    public void setup() {
        central = HomeCentral.getInstance();
        philipsFactory = DeviceFactoryProducer.getFactory("PHILIPS");

        // Criar dispositivos para teste
        devices = new ArrayList<>();
        LightDevice light = philipsFactory.createLight("Strategy Test Light");
        ThermostatDevice thermostat = philipsFactory.createThermostat("Strategy Test Thermostat");

        devices.add(light);
        devices.add(thermostat);

        // Registrar dispositivos
        central.getDeviceManager().registerDevice(light);
        central.getDeviceManager().registerDevice(thermostat);
    }

    @Test
    public void testBalancedPowerStrategy() {
        // Criar estratégia
        PowerSavingStrategy strategy = new BalancedPowerStrategy();

        // Verificar nome
        assertEquals("Balanced Power Saving", strategy.getName(), "Strategy should have correct name");

        // Aplicar a um dispositivo
        AbstractDevice light = devices.get(0);
        String result = strategy.applyToDevice(light);

        // Verificar resultados
        assertTrue(result.contains("Applied Balanced strategy"), "Should report strategy applied");
        assertTrue(light.isActive(), "Device should be active in balanced mode");

        if (light instanceof LightDevice) {
            assertEquals(50, ((LightDevice) light).getBrightness(), "Light should have 50% brightness");
        }

        // Aplicar a um termostato
        AbstractDevice thermostat = devices.get(1);
        result = strategy.applyToDevice(thermostat);

        // Verificar resultados
        assertTrue(result.contains("Applied Balanced strategy"), "Should report strategy applied");
        assertTrue(thermostat.isActive(), "Device should be active in balanced mode");

        if (thermostat instanceof ThermostatDevice) {
            assertEquals(21.0, ((ThermostatDevice) thermostat).getTargetTemperature(), "Thermostat should be set to 21°C");
        }

        // Aplicar a coleção de dispositivos
        result = strategy.applyToDevices(devices);

        // Verificar resultados
        assertTrue(result.contains("Strategy Test Light"), "Should include all devices");
        assertTrue(result.contains("Strategy Test Thermostat"), "Should include all devices");
    }

    @Test
    public void testMaxEconomyPowerStrategy() {
        // Criar estratégia
        PowerSavingStrategy strategy = new MaxEconomyPowerStrategy();

        // Verificar nome
        assertEquals("Maximum Power Saving", strategy.getName(), "Strategy should have correct name");

        // Aplicar a um dispositivo
        AbstractDevice light = devices.get(0);
        String result = strategy.applyToDevice(light);

        // Verificar resultados
        assertTrue(result.contains("Applied Max Economy strategy"), "Should report strategy applied");
        assertFalse(light.isActive(), "Device should be inactive in max economy mode");

        if (light instanceof LightDevice) {
            assertEquals(0, ((LightDevice) light).getBrightness(), "Light should have 0% brightness");
        }

        // Aplicar a um termostato
        AbstractDevice thermostat = devices.get(1);
        result = strategy.applyToDevice(thermostat);

        // Verificar resultados
        assertTrue(result.contains("Applied Max Economy strategy"), "Should report strategy applied");
        assertFalse(thermostat.isActive(), "Device should be inactive in max economy mode");

        if (thermostat instanceof ThermostatDevice) {
            assertEquals(18.0, ((ThermostatDevice) thermostat).getTargetTemperature(), "Thermostat should be set to 18°C");
        }
    }

    @Test
    public void testComfortPowerStrategy() {
        // Criar estratégia
        PowerSavingStrategy strategy = new ComfortPowerStrategy();

        // Verificar nome
        assertEquals("Comfort Mode", strategy.getName(), "Strategy should have correct name");

        // Aplicar a um dispositivo
        AbstractDevice light = devices.get(0);
        String result = strategy.applyToDevice(light);

        // Verificar resultados
        assertTrue(result.contains("Applied Comfort strategy"), "Should report strategy applied");
        assertTrue(light.isActive(), "Device should be active in comfort mode");

        if (light instanceof LightDevice) {
            assertEquals(100, ((LightDevice) light).getBrightness(), "Light should have 100% brightness");
        }

        // Aplicar a um termostato
        AbstractDevice thermostat = devices.get(1);
        result = strategy.applyToDevice(thermostat);

        // Verificar resultados
        assertTrue(result.contains("Applied Comfort strategy"), "Should report strategy applied");
        assertTrue(thermostat.isActive(), "Device should be active in comfort mode");

        if (thermostat instanceof ThermostatDevice) {
            assertEquals(23.0, ((ThermostatDevice) thermostat).getTargetTemperature(), "Thermostat should be set to 23°C");
        }
    }

    @Test
    public void testPowerManager() {
        // Criar gerenciador
        PowerManager manager = new PowerManager();

        // Verificar estratégia padrão
        assertTrue(manager.getCurrentStrategy() instanceof BalancedPowerStrategy,
                "Default strategy should be Balanced");

        // Verificar estratégias disponíveis
        Map<String, PowerSavingStrategy> strategies = manager.getAvailableStrategies();
        assertTrue(strategies.size() >= 3, "Should have at least 3 strategies");
        assertTrue(strategies.containsKey("balanced power saving"), "Should have balanced strategy");
        assertTrue(strategies.containsKey("maximum power saving"), "Should have max economy strategy");
        assertTrue(strategies.containsKey("comfort mode"), "Should have comfort strategy");

        // Testar mudança de estratégia
        manager.setStrategy("Maximum Power Saving");
        assertTrue(manager.getCurrentStrategy() instanceof MaxEconomyPowerStrategy,
                "Strategy should change to MaxEconomy");

        // Testar aplicação de estratégia a dispositivo único
        AbstractDevice light = devices.get(0);
        String result = manager.applyStrategy(light);
        assertTrue(result.contains("Max Economy"), "Should apply max economy strategy");
        assertFalse(light.isActive(), "Light should be off in max economy mode");

        // Testar aplicação de estratégia a coleção
        // Primeiro, ligar todos os dispositivos
        for (AbstractDevice device : devices) {
            device.execute("ON");
        }

        // Aplicar estratégia max economy a todos
        result = manager.applyStrategy(devices);

        // Verificar que todos os dispositivos foram desligados
        for (AbstractDevice device : devices) {
            assertFalse(device.isActive(), "All devices should be off in max economy mode");
        }

        // Testar estratégia de conforto
        manager.setStrategy("comfort mode");
        result = manager.applyStrategy(devices);

        // Verificar que todos os dispositivos foram ligados
        for (AbstractDevice device : devices) {
            assertTrue(device.isActive(), "All devices should be on in comfort mode");
        }

        // Testar estratégia inválida
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.setStrategy("NonexistentStrategy");
        });
        assertTrue(exception.getMessage().contains("Unknown strategy"),
                "Should throw exception for unknown strategy");
    }

    @Test
    public void testFlyweightRoomAttributes() {
        // Limpar cache para testes consistentes
        RoomAttributesFactory.clearCache();

        // Verificar tamanho inicial do cache
        assertEquals(0, RoomAttributesFactory.getCacheSize(), "Cache should be empty initially");

        // Obter atributos para um tipo de cômodo
        RoomAttributes livingRoom = RoomAttributesFactory.getRoomAttributes("Living Room");

        // Verificar atributos criados
        assertEquals("Living Room", livingRoom.getType(), "Should have correct type");
        assertEquals("60%", livingRoom.getDefaultLightLevel(), "Should have correct light level");
        assertEquals("22C", livingRoom.getDefaultTemperature(), "Should have correct temperature");
        assertEquals("Beige", livingRoom.getWallColor(), "Should have correct wall color");
        assertEquals("Hardwood", livingRoom.getFloorType(), "Should have correct floor type");

        // Verificar cache após primeira criação
        assertEquals(1, RoomAttributesFactory.getCacheSize(), "Cache should have one entry");

        // Obter novamente os mesmos atributos
        RoomAttributes livingRoom2 = RoomAttributesFactory.getRoomAttributes("Living Room");

        // Verificar que é o mesmo objeto (referência)
        assertSame(livingRoom, livingRoom2, "Should return cached instance");

        // Verificar cache após segunda solicitação
        assertEquals(1, RoomAttributesFactory.getCacheSize(), "Cache should still have one entry");

        // Obter atributos para outro tipo de cômodo
        RoomAttributes bedroom = RoomAttributesFactory.getRoomAttributes("Bedroom");

        // Verificar atributos diferentes
        assertEquals("Bedroom", bedroom.getType(), "Should have correct type");
        assertEquals("40%", bedroom.getDefaultLightLevel(), "Should have correct light level");
        assertEquals("20C", bedroom.getDefaultTemperature(), "Should have correct temperature");

        // Verificar cache após adição de outro tipo
        assertEquals(2, RoomAttributesFactory.getCacheSize(), "Cache should have two entries");

        // Verificar que objetos são diferentes para tipos diferentes
        assertNotSame(livingRoom, bedroom, "Should be different objects for different types");

        // Testar toString
        String roomString = livingRoom.toString();
        assertTrue(roomString.contains("RoomType: Living Room"), "ToString should include type");
        assertTrue(roomString.contains("LightLevel: 60%"), "ToString should include light level");

        // Testar tipo não definido explicitamente
        RoomAttributes custom = RoomAttributesFactory.getRoomAttributes("Custom");
        assertEquals("Custom", custom.getType(), "Should have correct type");
        assertEquals("50%", custom.getDefaultLightLevel(), "Should use default light level");
        assertEquals("21C", custom.getDefaultTemperature(), "Should use default temperature");
        assertEquals("White", custom.getWallColor(), "Should use default wall color");
        assertEquals("Wood", custom.getFloorType(), "Should use default floor type");
    }
}