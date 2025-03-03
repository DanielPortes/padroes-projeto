package trabalhofinal.smarthome.decorators;

public interface EnergyMonitored {
    double getCurrentPowerUsage();
    java.util.List<EnergyMonitoringDecorator.EnergyUsageRecord> getUsageHistory();
}