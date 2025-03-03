/**
 * Estratégia: Modo de economia máxima
 */
package trabalhofinal.smarthome.strategy;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.Collection;
import java.util.stream.Collectors;

public class MaxEconomyPowerStrategy implements PowerSavingStrategy {
    @Override
    public String getName() {
        return "Maximum Power Saving";
    }

    @Override
    public String applyToDevice(AbstractDevice device) {
        String result = device.execute("OFF");

        // Desligar dispositivos não essenciais
        if (device instanceof trabalhofinal.smarthome.devices.LightDevice) {
            trabalhofinal.smarthome.devices.LightDevice light = (trabalhofinal.smarthome.devices.LightDevice) device;
            light.setBrightness(0);
        } else if (device instanceof trabalhofinal.smarthome.devices.ThermostatDevice) {
            trabalhofinal.smarthome.devices.ThermostatDevice thermostat = (trabalhofinal.smarthome.devices.ThermostatDevice) device;
            // Configurar temperatura para o modo econômico
            thermostat.setTargetTemperature(18.0); // 18°C para economia de energia
        }

        return "Applied Max Economy strategy to " + device.getName() + ": " + result;
    }

    @Override
    public String applyToDevices(Collection<AbstractDevice> devices) {
        return devices.stream()
                .map(this::applyToDevice)
                .collect(Collectors.joining("\n"));
    }
}