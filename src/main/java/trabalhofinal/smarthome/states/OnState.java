/**
 * Estado Ligado
 */
package trabalhofinal.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.logging.Logger;

public class OnState implements DeviceState {
    private static final Logger LOGGER = Logger.getLogger(OnState.class.getName());

    private final AbstractDevice device;

    public OnState(AbstractDevice device) {
        this.device = device;
        LOGGER.fine("Device " + device.getName() + " entered ON state");
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
        LOGGER.fine("Device " + device.getName() + " handling command: " + command + " in ON state");

        if (command.equalsIgnoreCase("OFF")) {
            LOGGER.fine("Transitioning device " + device.getName() + " to OFF state");
            device.setState(new OffState(device));
            return "Device turned off";
        } else if (command.equalsIgnoreCase("STANDBY")) {
            LOGGER.fine("Transitioning device " + device.getName() + " to STANDBY state");
            device.setState(new StandbyState(device));
            return "Device set to standby";
        } else if (command.equalsIgnoreCase("ON")) {
            // Already on, just acknowledge
            LOGGER.fine("Device " + device.getName() + " already ON, ignoring ON command");
            return "Device is already on";
        } else {
            return "Device is on and cannot process: " + command;
        }
    }

    @Override
    public void updateState() {
        // No automatic state changes from ON state
    }
}