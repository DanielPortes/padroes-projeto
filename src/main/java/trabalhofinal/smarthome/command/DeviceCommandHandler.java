/**
 * Manipulador para comandos de dispositivos
 */
package trabalhofinal.smarthome.command;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.Optional;
import java.util.logging.Logger;

public class DeviceCommandHandler extends AbstractCommandHandler {
    private static final Logger LOGGER = Logger.getLogger(DeviceCommandHandler.class.getName());

    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("DEVICE")) {
            LOGGER.fine("Handling device command: " + command);
            trabalhofinal.smarthome.core.DeviceManager deviceManager = trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager();
            Optional<AbstractDevice> deviceOpt = deviceManager.getDeviceByName(command.getTarget());

            if (deviceOpt.isPresent()) {
                AbstractDevice device = deviceOpt.get();
                String action = command.getParameter("action");

                if (action != null) {
                    // Store initial state for logging
                    boolean initialState = device.isActive();
                    LOGGER.fine("Device " + device.getName() + " initial state: " + initialState);

                    // Execute the command
                    String result = device.execute(action);

                    // Log the result and new state
                    boolean newState = device.isActive();
                    LOGGER.fine("Device " + device.getName() + " new state: " + newState + ", result: " + result);

                    // If the expected state change didn't happen, force it
                    boolean expectedActive = action.equalsIgnoreCase("ON");
                    if (newState != expectedActive) {
                        LOGGER.warning("Device " + device.getName() + " state inconsistent after command " + action);
                        LOGGER.warning("Forcing correct state: " + expectedActive);

                        // Force the state to match the command
                        if (expectedActive) {
                            device.execute("ON");
                        } else {
                            device.execute("OFF");
                        }

                        // Verify force worked
                        if (device.isActive() != expectedActive) {
                            LOGGER.severe("Failed to force device state even after retry: " + device.getName());
                        }
                    }

                    return result;
                } else {
                    LOGGER.warning("Missing action parameter for device command: " + command);
                    return "Missing action parameter for device command";
                }
            } else {
                LOGGER.warning("Device not found: " + command.getTarget());
                return "Device not found: " + command.getTarget();
            }
        }

        LOGGER.fine("Command " + command.getType() + " not handled, passing to next handler");
        return passToNext(command);
    }
}