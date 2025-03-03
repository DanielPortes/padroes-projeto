/**
 * Estratégia: Modo balanceado
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

public class BalancedPowerStrategy implements PowerSavingStrategy {
    @Override
    public String getName() {
        return "Balanced Power Saving";
    }

    @Override
    public String applyToDevice(AbstractDevice device) {
        String result = device.execute("ON");

        // Configurar dispositivos para uso moderado
        if (device instanceof trabalhofinal.smarthome.devices.LightDevice) {
            trabalhofinal.smarthome.devices.LightDevice light = (trabalhofinal.smarthome.devices.LightDevice) device;
            light.setBrightness(50); // 50% de brilho
        } else if (device instanceof trabalhofinal.smarthome.devices.ThermostatDevice) {
            trabalhofinal.smarthome.devices.ThermostatDevice thermostat = (trabalhofinal.smarthome.devices.ThermostatDevice) device;
            thermostat.setTargetTemperature(21.0); // 21°C - temperatura confortável e econômica
        }

        return "Applied Balanced strategy to " + device.getName() + ": " + result;
    }

    @Override
    public String applyToDevices(Collection<AbstractDevice> devices) {
        return devices.stream()
                .map(this::applyToDevice)
                .collect(Collectors.joining("\n"));
    }
}
