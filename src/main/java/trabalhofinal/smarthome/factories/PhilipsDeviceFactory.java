/**
 * Implementação de fábrica para dispositivos Philips
 */

package trabalhofinal.smarthome.factories;
//package com.smarthome.factories;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.PhilipsImplementation;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.devices.PhilipsImplementation;

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