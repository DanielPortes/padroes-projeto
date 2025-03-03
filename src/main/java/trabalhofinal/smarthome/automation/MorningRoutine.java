/**
 * Rotina baseada em tempo
 */
package trabalhofinal.smarthome.automation;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.Room;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class MorningRoutine extends AutomationRoutine {
    private final LocalTime startTime;
    private final String[] targetRooms;

    public MorningRoutine(LocalTime startTime, String... targetRooms) {
        super("Morning Routine");
        this.startTime = startTime;
        this.targetRooms = targetRooms;
    }

    @Override
    protected boolean shouldExecute() {
        LocalTime now = LocalTime.now();
        return now.isAfter(startTime) && now.isBefore(startTime.plusHours(1));
    }

    @Override
    protected String beforeRoutine() {
        return "Preparing for morning routine at " + LocalDateTime.now();
    }
    @Override
    protected String executeRoutineSteps() {
        StringBuilder result = new StringBuilder();
        boolean atLeastOneRoomFound = false;

        for (String roomName : targetRooms) {
            Optional<Room> room = HomeCentral.getInstance().getRoomManager().getRoomByName(roomName);

            if (room.isPresent()) {
                atLeastOneRoomFound = true;
                result.append("Activating room: ").append(roomName).append("\n");
                result.append(room.get().turnAllDevicesOn()).append("\n");
            } else {
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

        return "Morning routine completed at " + LocalDateTime.now();
    }
}