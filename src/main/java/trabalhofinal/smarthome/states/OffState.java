/**
 * Estado Desligado
 */
package trabalhofinal.smarthome.states;
//package com.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

public class OffState implements DeviceState {
    private final AbstractDevice device;

    public OffState(AbstractDevice device) {
        this.device = device;
    }

    @Override
    public String getName() {
        return "OFF";
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String handleCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            device.setState(new OnState(device));
            return "Device turned on";
        }
        return "Device is off and cannot process: " + command;
    }

    @Override
    public void updateState() {
        // Implementação para mudanças automáticas baseadas em condições
    }
}
