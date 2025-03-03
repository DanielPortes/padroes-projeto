
/**
 * Implementação de fábrica para dispositivos Samsung
 */
package trabalhofinal.smarthome.factories;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

public class SamsungDeviceFactory implements DeviceFactory {
    @Override
    public LightDevice createLight(String name) {
        return new LightDevice(name, new trabalhofinal.smarthome.devices.SamsungImplementation());
    }

    @Override
    public ThermostatDevice createThermostat(String name) {
        return new ThermostatDevice(name, new trabalhofinal.smarthome.devices.SamsungImplementation());
    }
}