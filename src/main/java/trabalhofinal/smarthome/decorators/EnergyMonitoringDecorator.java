/**
 * Decorator que adiciona monitoramento de energia
 */
package trabalhofinal.smarthome.decorators;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnergyMonitoringDecorator extends DeviceDecorator {
    private double currentPowerUsage;
    private final List<EnergyUsageRecord> usageHistory;

    public EnergyMonitoringDecorator(AbstractDevice decoratedDevice) {
        super(decoratedDevice);
        this.currentPowerUsage = 0.0;
        this.usageHistory = new ArrayList<>();
    }

    @Override
    public String execute(String command) {
        String result = super.execute(command);

        // Simular mudança no consumo de energia baseado no comando
        if (command.equalsIgnoreCase("ON")) {
            updatePowerUsage(getBasePowerUsage());
        } else if (command.equalsIgnoreCase("OFF")) {
            updatePowerUsage(0.0);
        }

        return result;
    }

    private double getBasePowerUsage() {
        // Simular diferentes consumos para diferentes tipos de dispositivos
        switch (getType()) {
            case "Light": return 10.0;
            case "Thermostat": return 50.0;
            default: return 20.0;
        }
    }

    private void updatePowerUsage(double newUsage) {
        if (currentPowerUsage != newUsage) {
            recordUsage();
            currentPowerUsage = newUsage;
        }
    }

    private void recordUsage() {
        usageHistory.add(new EnergyUsageRecord(
                LocalDateTime.now(),
                currentPowerUsage
        ));
    }

    public double getCurrentPowerUsage() {
        return currentPowerUsage;
    }

    public List<EnergyUsageRecord> getUsageHistory() {
        return Collections.unmodifiableList(usageHistory);
    }

    @Override
    public String getStatus() {
        return super.getStatus() + "Current Power Usage: " + currentPowerUsage + " Watts\n";
    }

    public static class EnergyUsageRecord {
        private final LocalDateTime timestamp;
        private final double powerUsage;

        public EnergyUsageRecord(LocalDateTime timestamp, double powerUsage) {
            this.timestamp = timestamp;
            this.powerUsage = powerUsage;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getPowerUsage() {
            return powerUsage;
        }
    }
}