/**
 * Implementação concreta de dispositivo de temperatura
 */
package trabalhofinal.smarthome.devices;

public class ThermostatDevice extends AbstractDevice {
    private double temperature;
    private double targetTemperature;

    public ThermostatDevice(String name, DeviceImplementation implementation) {
        super(name, implementation);
        this.temperature = 21.0;
        this.targetTemperature = 21.0;
    }

    @Override
    public String getType() {
        return "Thermostat";
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    @Override
    public String getStatus() {
        return super.getStatus() +
                String.format("Temperature: %.1f°C\nTarget: %.1f°C\n",
                        temperature, targetTemperature);
    }
}
