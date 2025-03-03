/**
 * Rotina baseada em tempo
 */
package trabalhofinal.smarthome.automation;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.devices.AbstractDevice;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.logging.Logger;

public class MorningRoutine extends AutomationRoutine {
    private static final Logger LOGGER = Logger.getLogger(MorningRoutine.class.getName());

    private final LocalTime startTime;
    private final String[] targetRooms;

    public MorningRoutine(LocalTime startTime, String... targetRooms) {
        super("Morning Routine");
        this.startTime = startTime;
        this.targetRooms = targetRooms;
        LOGGER.fine("Created Morning Routine with start time: " + startTime);
    }

    @Override
    protected boolean shouldExecute() {
        LocalTime now = LocalTime.now();
        boolean shouldRun = now.isAfter(startTime) && now.isBefore(startTime.plusHours(1));
        LOGGER.fine("Morning Routine shouldExecute(): " + shouldRun + " (current time: " + now + ")");
        return shouldRun;
    }

    @Override
    protected String beforeRoutine() {
        LOGGER.fine("Morning Routine starting");
        return "Preparing for morning routine at " + LocalDateTime.now();
    }

    @Override
    protected String executeRoutineSteps() {
        StringBuilder result = new StringBuilder();

        for (String roomName : targetRooms) {
            LOGGER.fine("Looking for room: " + roomName);
            Optional<Room> roomOpt = HomeCentral.getInstance().getRoomManager().getRoomByName(roomName);

            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                LOGGER.fine("Activating room: " + roomName + " with " + room.getDevices().size() + " devices");
                result.append("Activating room: ").append(roomName).append("\n");

                // Get device state before activation for logging
                for (AbstractDevice device : room.getDevices()) {
                    LOGGER.fine("Before activation - Device: " + device.getName() + " state: " + device.isActive());
                }

                // Activate the room
                String turnOnResult = room.turnAllDevicesOn();
                result.append(turnOnResult).append("\n");

                // Verify all devices are on
                for (AbstractDevice device : room.getDevices()) {
                    if (!device.isActive()) {
                        LOGGER.warning("Device not active after room activation: " + device.getName());
                        LOGGER.warning("Forcing device on: " + device.getName());
                        device.execute("ON");
                    }
                }

                // Log final device states
                for (AbstractDevice device : room.getDevices()) {
                    LOGGER.fine("After activation - Device: " + device.getName() + " state: " + device.isActive());
                }
            } else {
                LOGGER.warning("Room not found: " + roomName);
                result.append("Room not found: ").append(roomName).append("\n");
            }
        }

        return result.toString();
    }

    @Override
    protected String afterRoutine() {
        // Notificar sobre a execução
        HomeCentral.getInstance().getNotificationCenter()
                .sendNotification("Morning routine completed successfully");

        LOGGER.fine("Morning Routine completed");
        return "Morning routine completed at " + LocalDateTime.now();
    }
}