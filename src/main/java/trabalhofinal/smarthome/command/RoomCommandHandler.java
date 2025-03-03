/**
 * Manipulador para comandos de cômodos
 */
package trabalhofinal.smarthome.command;

import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.core.RoomManager;
import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.Optional;
import java.util.logging.Logger;

public class RoomCommandHandler extends AbstractCommandHandler {
    private static final Logger LOGGER = Logger.getLogger(RoomCommandHandler.class.getName());

    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("ROOM")) {
            LOGGER.info("Handling room command: " + command);

            RoomManager roomManager = trabalhofinal.smarthome.core.HomeCentral.getInstance().getRoomManager();
            Optional<Room> roomOpt = roomManager.getRoomByName(command.getTarget());

            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                String action = command.getParameter("action");

                if (action != null) {
                    String result;

                    switch (action.toUpperCase()) {
                        case "ON":
                            LOGGER.info("Turning ON all devices in room: " + room.getName());
                            result = room.turnAllDevicesOn();

                            // Verify all devices turned on
                            verifyDeviceStates(room, true);
                            break;

                        case "OFF":
                            LOGGER.info("Turning OFF all devices in room: " + room.getName());
                            result = room.turnAllDevicesOff();

                            // Verify all devices turned off
                            verifyDeviceStates(room, false);
                            break;

                        default:
                            LOGGER.warning("Unknown room action: " + action);
                            result = "Unknown room action: " + action;
                            break;
                    }

                    return result;
                } else {
                    LOGGER.warning("Missing action parameter for room command");
                    return "Missing action parameter for room command";
                }
            } else {
                LOGGER.warning("Room not found: " + command.getTarget());
                return "Room not found: " + command.getTarget();
            }
        }

        LOGGER.fine("Command not handled, passing to next handler: " + command);
        return passToNext(command);
    }

    /**
     * Verify all devices in a room have the expected state and force if needed
     */
    private void verifyDeviceStates(Room room, boolean expectedState) {
        LOGGER.info("Verifying devices in room " + room.getName() + " are " +
                (expectedState ? "ON" : "OFF"));

        for (AbstractDevice device : room.getDevices()) {
            boolean actualState = device.isActive();
            LOGGER.info("Device " + device.getName() + " state: " + actualState);

            if (actualState != expectedState) {
                LOGGER.warning("Device " + device.getName() + " has incorrect state, forcing to " +
                        (expectedState ? "ON" : "OFF"));

                if (expectedState) {
                    device.execute("ON");
                } else {
                    device.execute("OFF");
                }

                // Verify fix worked
                if (device.isActive() != expectedState) {
                    LOGGER.severe("Failed to force state for device: " + device.getName());
                }
            }
        }
    }
}