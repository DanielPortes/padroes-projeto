/**
 * Manipulador para comandos de cômodos
 */
package trabalhofinal.smarthome.command;
//package com.smarthome.command;

//import com.smarthome.core.HomeCentral;
//import com.smarthome.core.Room;
//import com.smarthome.core.RoomManager;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.RoomManager;
import trabalhofinal.smarthome.visitor.Room;

import java.util.Optional;

public class RoomCommandHandler extends AbstractCommandHandler {
    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("ROOM")) {
            trabalhofinal.smarthome.core.RoomManager roomManager = trabalhofinal.smarthome.core.HomeCentral.getInstance().getRoomManager();
            Optional<trabalhofinal.smarthome.visitor.Room> room = roomManager.getRoomByName(command.getTarget());

            if (room.isPresent()) {
                String action = command.getParameter("action");
                if (action != null) {
                    switch (action.toUpperCase()) {
                        case "ON":
                            return room.get().turnAllDevicesOn();
                        case "OFF":
                            return room.get().turnAllDevicesOff();
                        default:
                            return "Unknown room action: " + action;
                    }
                } else {
                    return "Missing action parameter for room command";
                }
            } else {
                return "Room not found: " + command.getTarget();
            }
        }

        return passToNext(command);
    }
}
