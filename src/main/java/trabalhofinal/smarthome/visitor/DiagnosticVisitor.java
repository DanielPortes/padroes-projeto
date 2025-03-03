/**
 * Implementação concreta de Visitor para diagnóstico
 */
package trabalhofinal.smarthome.visitor;
//package com.smarthome.visitor;

import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

public class DiagnosticVisitor implements HomeVisitor {
    private int totalDevicesVisited = 0;
    private int activeDevicesVisited = 0;

    @Override
    public String visitLight(LightDevice light) {
        totalDevicesVisited++;
        if (light.isActive()) {
            activeDevicesVisited++;
        }

        StringBuilder report = new StringBuilder();
        report.append("DIAGNOSTIC - Light Device: ").append(light.getName()).append("\n");
        report.append("  Status: ").append(light.isActive() ? "ON" : "OFF").append("\n");
        report.append("  Brightness: ").append(light.getBrightness()).append("%\n");
        report.append("  Health: ").append(diagnoseDeviceHealth(light)).append("\n");

        return report.toString();
    }

    @Override
    public String visitThermostat(ThermostatDevice thermostat) {
        totalDevicesVisited++;
        if (thermostat.isActive()) {
            activeDevicesVisited++;
        }

        StringBuilder report = new StringBuilder();
        report.append("DIAGNOSTIC - Thermostat Device: ").append(thermostat.getName()).append("\n");
        report.append("  Status: ").append(thermostat.isActive() ? "ON" : "OFF").append("\n");
        report.append("  Current Temperature: ").append(thermostat.getTemperature()).append("°C\n");
        report.append("  Target Temperature: ").append(thermostat.getTargetTemperature()).append("°C\n");
        report.append("  Efficiency: ").append(calculateThermostatEfficiency(thermostat)).append("%\n");

        return report.toString();
    }

    @Override
    public String visitRoom(Room room) {
        StringBuilder report = new StringBuilder();
        report.append("DIAGNOSTIC - Room: ").append(room.getName()).append("\n");
        report.append("  Type: ").append(room.getAttributes().getType()).append("\n");
        report.append("  Active Devices: ").append(room.getActiveDeviceCount()).append("/");
        report.append(room.getDevices().size()).append("\n");

        report.append("  Devices in Room:\n");
        for (com.smarthome.devices.AbstractDevice device : room.getDevices()) {
            report.append("    - ").append(device.getName());
            report.append(" (").append(device.isActive() ? "ON" : "OFF").append(")\n");
        }

        return report.toString();
    }

    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("DIAGNOSTIC SUMMARY\n");
        summary.append("Total Devices Visited: ").append(totalDevicesVisited).append("\n");
        summary.append("Active Devices: ").append(activeDevicesVisited).append("\n");
        summary.append("Inactive Devices: ").append(totalDevicesVisited - activeDevicesVisited).append("\n");

        if (totalDevicesVisited > 0) {
            int activePercentage = (activeDevicesVisited * 100) / totalDevicesVisited;
            summary.append("System Activity Level: ").append(activePercentage).append("%\n");
        }

        return summary.toString();
    }

    private String diagnoseDeviceHealth(LightDevice light) {
        // Simulação de diagnóstico baseado no brilho
        if (light.getBrightness() < 10) {
            return "Check bulb, possible failure";
        } else if (light.getBrightness() < 30) {
            return "Low brightness, possible degradation";
        }
        return "Normal";
    }

    private int calculateThermostatEfficiency(ThermostatDevice thermostat) {
        // Simulação de cálculo de eficiência
        double diff = Math.abs(thermostat.getTemperature() - thermostat.getTargetTemperature());
        if (diff <= 0.5) {
            return 95;
        } else if (diff <= 1.0) {
            return 85;
        } else if (diff <= 2.0) {
            return 70;
        }
        return 50;
    }
}

