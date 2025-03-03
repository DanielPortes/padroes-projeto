
package trabalhofinal.smarthome.strategy;
//package com.smarthome.strategy;

import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.devices.LightDevice;

import java.util.Collection;

/**
 * Interface Strategy para diferentes modos de economia de energia
 */
public interface PowerSavingStrategy {
    String getName();
    String applyToDevice(AbstractDevice device);
    String applyToDevices(Collection<AbstractDevice> devices);
}


