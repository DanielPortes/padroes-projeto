/**
 * Implementação concreta de Visitor para relatórios de energia
 */
package trabalhofinal.smarthome.visitor;

import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.decorators.EnergyMonitoringDecorator;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

import java.util.HashMap;
import java.util.Map;

public class EnergyReportVisitor implements HomeVisitor {
    private double totalEnergyUsage = 0.0;
    private final Map<String, Double> roomEnergyUsage = new HashMap<>();

    @Override
    public String visitLight(LightDevice light) {
        double energyUsage = estimateEnergyUsage(light);
        totalEnergyUsage += energyUsage;

        StringBuilder report = new StringBuilder();
        report.append("ENERGY - Light: ").append(light.getName()).append("\n");
        report.append("  Status: ").append(light.isActive() ? "ON" : "OFF").append("\n");
        report.append("  Brightness: ").append(light.getBrightness()).append("%\n");
        report.append("  Estimated Power: ").append(String.format("%.2f", energyUsage)).append(" Watts\n");

        // Verificar se é um dispositivo com monitoramento de energia
        if (light instanceof EnergyMonitoringDecorator) {
            EnergyMonitoringDecorator monitoredLight = (EnergyMonitoringDecorator) light;
            report.append("  Measured Power: ").append(String.format("%.2f", monitoredLight.getCurrentPowerUsage())).append(" Watts\n");
        }

        return report.toString();
    }

    @Override
    public String visitThermostat(ThermostatDevice thermostat) {
        double energyUsage = estimateEnergyUsage(thermostat);
        totalEnergyUsage += energyUsage;

        StringBuilder report = new StringBuilder();
        report.append("ENERGY - Thermostat: ").append(thermostat.getName()).append("\n");
        report.append("  Status: ").append(thermostat.isActive() ? "ON" : "OFF").append("\n");
        report.append("  Temperature Differential: ").append(
                String.format("%.1f", Math.abs(thermostat.getTemperature() - thermostat.getTargetTemperature()))).append("°C\n");
        report.append("  Estimated Power: ").append(String.format("%.2f", energyUsage)).append(" Watts\n");

        // Verificar se é um dispositivo com monitoramento de energia
        if (thermostat instanceof EnergyMonitoringDecorator) {
            EnergyMonitoringDecorator monitoredThermostat = (EnergyMonitoringDecorator) thermostat;
            report.append("  Measured Power: ").append(String.format("%.2f", monitoredThermostat.getCurrentPowerUsage())).append(" Watts\n");
        }

        return report.toString();
    }

    @Override
    public String visitRoom(Room room) {
        double roomEnergy = 0.0;

        // Calcular energia usada por todos os dispositivos no cômodo
        for (trabalhofinal.smarthome.devices.AbstractDevice device : room.getDevices()) {
            if (device instanceof LightDevice) {
                roomEnergy += estimateEnergyUsage((LightDevice) device);
            } else if (device instanceof ThermostatDevice) {
                roomEnergy += estimateEnergyUsage((ThermostatDevice) device);
            }
        }

        roomEnergyUsage.put(room.getName(), roomEnergy);

        StringBuilder report = new StringBuilder();
        report.append("ENERGY - Room: ").append(room.getName()).append("\n");
        report.append("  Total Devices: ").append(room.getDevices().size()).append("\n");
        report.append("  Active Devices: ").append(room.getActiveDeviceCount()).append("\n");
        report.append("  Total Energy Usage: ").append(String.format("%.2f", roomEnergy)).append(" Watts\n");

        return report.toString();
    }

    public String getEnergyReport() {
        StringBuilder report = new StringBuilder();
        report.append("SMART HOME ENERGY REPORT\n");
        report.append("Total Energy Usage: ").append(String.format("%.2f", totalEnergyUsage)).append(" Watts\n\n");

        report.append("Energy Usage by Room:\n");
        roomEnergyUsage.forEach((room, energy) ->
                report.append("  - ").append(room).append(": ")
                        .append(String.format("%.2f", energy)).append(" Watts (")
                        .append(String.format("%.1f", (energy / totalEnergyUsage) * 100)).append("%)\n")
        );

        return report.toString();
    }

    private double estimateEnergyUsage(LightDevice light) {
        if (!light.isActive()) {
            return 0.0;
        }

        // Estimar baseado no brilho (10W base + até 40W adicional baseado no brilho)
        return 10.0 + ((light.getBrightness() / 100.0) * 40.0);
    }

    private double estimateEnergyUsage(ThermostatDevice thermostat) {
        if (!thermostat.isActive()) {
            return 0.0;
        }

        // Estimar baseado na diferença de temperatura (maior diferença = maior consumo)
        double diff = Math.abs(thermostat.getTemperature() - thermostat.getTargetTemperature());
        return 50.0 + (diff * 20.0); // 50W base + 20W por grau de diferença
    }
}
