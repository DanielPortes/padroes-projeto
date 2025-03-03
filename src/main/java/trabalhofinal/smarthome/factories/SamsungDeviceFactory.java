
/**
 * Implementação de fábrica para dispositivos Samsung
 */
//package com.smarthome.factories;
package trabalhofinal.smarthome.factories;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.SamsungImplementation;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.devices.SamsungImplementation;

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