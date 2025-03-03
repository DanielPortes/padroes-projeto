package trabalhofinal.smarthome;


import trabalhofinal.smarthome.automation.MorningRoutine;
import trabalhofinal.smarthome.automation.SecurityAlertRoutine;
import trabalhofinal.smarthome.command.Command;
import trabalhofinal.smarthome.command.CommandProcessor;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.decorators.EnergyMonitoringDecorator;
import trabalhofinal.smarthome.decorators.SecurityEnabledDecorator;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.factories.DeviceFactory;
import trabalhofinal.smarthome.factories.DeviceFactoryProducer;
import trabalhofinal.smarthome.observer.LoggingObserver;
import trabalhofinal.smarthome.observer.MobileAppNotifier;
import trabalhofinal.smarthome.strategy.PowerManager;

import java.time.LocalTime;
import java.util.logging.Logger;

/**
 * Demonstração do sistema de automação residencial
 */
public class SmartHomeDemo {
    private static final Logger LOGGER = Logger.getLogger(SmartHomeDemo.class.getName());

    /**
     * Método de demonstração do sistema
     * @return Resultado da execução
     */
    public static String runDemo() {
        StringBuilder result = new StringBuilder();
        result.append("Iniciando demonstração do Sistema de Automação Residencial Inteligente\n\n");

        // Obter a instância central
        HomeCentral central = HomeCentral.getInstance();

        // Configurar observadores para notificações
        central.getNotificationCenter().addObserver(new LoggingObserver());
        central.getNotificationCenter().addObserver(
                new MobileAppNotifier("admin", message ->
                        result.append("MOBILE NOTIFICATION: ").append(message).append("\n"))
        );

        // Criar cômodos
        result.append("Configurando cômodos...\n");
        Room livingRoom = central.getRoomManager().createRoom("Sala de Estar", "Living Room");
        Room bedroom = central.getRoomManager().createRoom("Quarto Principal", "Bedroom");
        Room kitchen = central.getRoomManager().createRoom("Cozinha", "Kitchen");

        // Criar fábricas de dispositivos
        DeviceFactory philipsFactory = DeviceFactoryProducer.getFactory("Philips");
        DeviceFactory samsungFactory = DeviceFactoryProducer.getFactory("Samsung");

        // Criar e registrar dispositivos
        result.append("Configurando dispositivos...\n");

        // Dispositivos da sala
        LightDevice livingRoomLight = philipsFactory.createLight("Luz da Sala");
        ThermostatDevice livingRoomThermostat = philipsFactory.createThermostat("Termostato da Sala");

        // Aplicar decoradores para adicionar funcionalidades
        AbstractDevice secureLight = new SecurityEnabledDecorator(livingRoomLight);
        AbstractDevice monitoredThermostat = new EnergyMonitoringDecorator(livingRoomThermostat);

        // Adicionar dispositivos aos cômodos e registrar no sistema
        livingRoom.addDevice(secureLight);
        livingRoom.addDevice(monitoredThermostat);
        central.getDeviceManager().registerDevice(secureLight);
        central.getDeviceManager().registerDevice(monitoredThermostat);

        // Dispositivos do quarto
        LightDevice bedroomLight = samsungFactory.createLight("Luz do Quarto");
        ThermostatDevice bedroomThermostat = samsungFactory.createThermostat("Termostato do Quarto");

        bedroom.addDevice(bedroomLight);
        bedroom.addDevice(bedroomThermostat);
        central.getDeviceManager().registerDevice(bedroomLight);
        central.getDeviceManager().registerDevice(bedroomThermostat);

        // Dispositivos da cozinha
        LightDevice kitchenLight = samsungFactory.createLight("Luz da Cozinha");
        AbstractDevice monitoredKitchenLight = new EnergyMonitoringDecorator(kitchenLight);

        kitchen.addDevice(monitoredKitchenLight);
        central.getDeviceManager().registerDevice(monitoredKitchenLight);

        // Configurar rotinas automáticas
        result.append("Configurando rotinas automáticas...\n");
        MorningRoutine morningRoutine = new MorningRoutine(
                LocalTime.of(7, 0), "Quarto Principal", "Cozinha"
        );

        SecurityAlertRoutine securityRoutine = new SecurityAlertRoutine();

        // Demonstrar funcionamento do sistema
        result.append("\n===== DEMONSTRAÇÃO DE FUNCIONAMENTO =====\n\n");

        // Usar Estratégia para configurar modo de economia de energia
        result.append("Aplicando modo de economia de energia balanceado...\n");
        PowerManager powerManager = new PowerManager();
        result.append(powerManager.applyStrategy(central.getDeviceManager().getAllDevices()));
        result.append("\n\n");

        // Demonstrar Chain of Responsibility para processamento de comandos
        result.append("Processando comandos via Chain of Responsibility...\n");
        CommandProcessor commandProcessor = new CommandProcessor();

        // Comando para um dispositivo
        Command deviceCommand = new Command("DEVICE", "Luz da Sala")
                .addParameter("action", "ON");
        result.append(commandProcessor.processCommand(deviceCommand)).append("\n");

        // Comando para um cômodo
        Command roomCommand = new Command("ROOM", "Quarto Principal")
                .addParameter("action", "ON");
        result.append(commandProcessor.processCommand(roomCommand)).append("\n");

        // Ativar sistema de segurança
        result.append("\nAtivando sistema de segurança...\n");
        Command securityCommand = new Command("SECURITY", "System")
                .addParameter("action", "ARM");
        result.append(commandProcessor.processCommand(securityCommand)).append("\n");

        // Testar rotina automática
        result.append("\nExecutando rotina matinal simulada...\n");
        result.append(morningRoutine.execute()).append("\n");

        // Simular evento de alerta de segurança
        result.append("\nSimulando alerta de segurança...\n");
        central.getNotificationCenter().sendAlert("Sensor", "security breach detected at front door");

        // Retornar status do sistema
        result.append("\n===== STATUS DO SISTEMA =====\n");
        result.append(central.getSystemStatus()).append("\n");

        result.append("\n===== STATUS DOS CÔMODOS =====\n");
        result.append(livingRoom.getRoomStatus()).append("\n");
        result.append(bedroom.getRoomStatus()).append("\n");
        result.append(kitchen.getRoomStatus()).append("\n");

        result.append("\nDemonstração concluída com sucesso!");
        return result.toString();
    }
}