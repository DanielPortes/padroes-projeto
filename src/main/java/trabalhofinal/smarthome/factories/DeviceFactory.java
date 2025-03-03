package trabalhofinal.smarthome.factories;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

/**
 * Interface para fábricas de dispositivos (Abstract Factory)
 */
public interface DeviceFactory {
    LightDevice createLight(String name);
    ThermostatDevice createThermostat(String name);
}





