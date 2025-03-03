/**
 * Implementação de fábrica para dispositivos Philips
 */

package trabalhofinal.smarthome.factories;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

public class PhilipsDeviceFactory implements DeviceFactory {
    @Override
    public LightDevice createLight(String name) {
        return new LightDevice(name, new trabalhofinal.smarthome.devices.PhilipsImplementation());
    }

    @Override
    public ThermostatDevice createThermostat(String name) {
        return new ThermostatDevice(name, new trabalhofinal.smarthome.devices.PhilipsImplementation());
    }
}