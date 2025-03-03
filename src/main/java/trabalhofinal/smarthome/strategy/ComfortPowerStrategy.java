/**
 * Estratégia: Modo de conforto
 */
package trabalhofinal.smarthome.strategy;
//package com.smarthome.strategy;

import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

import java.util.Collection;
import java.util.stream.Collectors;

public class ComfortPowerStrategy implements PowerSavingStrategy {
    @Override
    public String getName() {
        return "Comfort Mode";
    }

    @Override
    public String applyToDevice(AbstractDevice device) {
        String result = device.execute("ON");

        // Configurar dispositivos para máximo conforto
        if (device instanceof trabalhofinal.smarthome.devices.LightDevice) {
            trabalhofinal.smarthome.devices.LightDevice light = (trabalhofinal.smarthome.devices.LightDevice) device;
            light.setBrightness(100); // 100% de brilho
        } else if (device instanceof trabalhofinal.smarthome.devices.ThermostatDevice) {
            trabalhofinal.smarthome.devices.ThermostatDevice thermostat = (trabalhofinal.smarthome.devices.ThermostatDevice) device;
            thermostat.setTargetTemperature(23.0); // 23°C - temperatura mais confortável
        }

        return "Applied Comfort strategy to " + device.getName() + ": " + result;
    }

    @Override
    public String applyToDevices(Collection<AbstractDevice> devices) {
        return devices.stream()
                .map(this::applyToDevice)
                .collect(Collectors.joining("\n"));
    }
}
