/**
 * Estado Ligado
 */
package trabalhofinal.smarthome.states;
//package com.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

public class OnState implements DeviceState {
    private final AbstractDevice device;

    public OnState(AbstractDevice device) {
        this.device = device;
    }

    @Override
    public String getName() {
        return "ON";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "OFF":
                device.setState(new OffState(device));
                return "Device turned off";
            case "STANDBY":
                device.setState(new StandbyState(device));
                return "Device set to standby";
            default:
                return "Device is already on";
        }
    }

    @Override
    public void updateState() {
        // Implementação para mudanças automáticas baseadas em condições
    }
}
