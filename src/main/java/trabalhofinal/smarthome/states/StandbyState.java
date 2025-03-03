/**
 * Estado Standby
 */
package trabalhofinal.smarthome.states;
//package com.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

public class StandbyState implements DeviceState {
    private final AbstractDevice device;

    public StandbyState(AbstractDevice device) {
        this.device = device;
    }

    @Override
    public String getName() {
        return "STANDBY";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "ON":
                device.setState(new OnState(device));
                return "Device turned on from standby";
            case "OFF":
                device.setState(new OffState(device));
                return "Device turned off from standby";
            default:
                return "Device is in standby mode";
        }
    }

    @Override
    public void updateState() {
        // Implementação para mudanças automáticas baseadas em condições
    }
}