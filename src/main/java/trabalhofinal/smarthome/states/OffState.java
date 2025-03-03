/**
 * Estado Desligado
 */
package trabalhofinal.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.logging.Logger;

public class OffState implements DeviceState {
    private static final Logger LOGGER = Logger.getLogger(OffState.class.getName());

    private final AbstractDevice device;

    public OffState(AbstractDevice device) {
        this.device = device;
        LOGGER.fine("Device " + device.getName() + " entered OFF state");
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
        LOGGER.fine("Device " + device.getName() + " handling command: " + command + " in OFF state");

        if (command.equalsIgnoreCase("ON")) {
            LOGGER.fine("Transitioning device " + device.getName() + " to ON state");
            device.setState(new OnState(device));

            // Verify transition
            if (device.isActive()) {
                LOGGER.fine("Successfully transitioned device " + device.getName() + " to ON state");
            } else {
                LOGGER.warning("Failed to transition device " + device.getName() + " to ON state");
            }

            return "Device turned on";
        } else if (command.equalsIgnoreCase("OFF")) {
            // Already off, just acknowledge
            LOGGER.fine("Device " + device.getName() + " already OFF, ignoring OFF command");
            return "Device is already off";
        } else {
            return "Device is off and cannot process: " + command;
        }
    }

    @Override
    public void updateState() {
        // No automatic state changes from OFF state
    }
}