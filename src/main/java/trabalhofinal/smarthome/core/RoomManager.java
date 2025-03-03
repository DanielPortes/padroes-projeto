/**
 * Gerenciador de cômodos
 */
package trabalhofinal.smarthome.core;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoomManager {
    private static final Logger LOGGER = Logger.getLogger(RoomManager.class.getName());
    private final Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
        LOGGER.fine("Room Manager initialized");
    }

    public Room createRoom(String name, String type) {
        Room room = new Room(name, type);
        rooms.put(room.getId(), room);
        LOGGER.fine("Created room: " + name + " (id: " + room.getId() + ")");
        return room;
    }

    public Optional<Room> getRoomById(String id) {
        Room room = rooms.get(id);
        LOGGER.fine("Looking up room by id: " + id + " - " + (room != null ? "found: " + room.getName() : "not found"));
        return Optional.ofNullable(room);
    }

    public Optional<Room> getRoomByName(String name) {
        LOGGER.fine("Looking up room by name: " + name);

        // Case-insensitive search
        Optional<Room> room = rooms.values().stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();

        if (room.isPresent()) {
            LOGGER.fine("Found room: " + room.get().getName() + " (id: " + room.get().getId() + ")");
        } else {
            LOGGER.warning("Room not found: " + name);
            // Log available rooms for debugging
            LOGGER.fine("Available rooms: " + rooms.values().stream()
                    .map(Room::getName)
                    .collect(Collectors.joining(", ")));
        }

        return room;
    }

    public List<Room> getAllRooms() {
        List<Room> allRooms = new ArrayList<>(rooms.values());
        LOGGER.fine("Retrieved all rooms: " + allRooms.size() + " rooms");
        return allRooms;
    }

    public List<Room> getRoomsByType(String type) {
        List<Room> roomsByType = rooms.values().stream()
                .filter(room -> room.getAttributes().getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());

        LOGGER.fine("Retrieved rooms by type: " + type + " - " + roomsByType.size() + " rooms");
        return roomsByType;
    }

    public void removeRoom(String id) {
        Room removedRoom = rooms.remove(id);
        if (removedRoom != null) {
            LOGGER.fine("Removed room: " + removedRoom.getName() + " (id: " + id + ")");
        } else {
            LOGGER.warning("Attempted to remove non-existent room with id: " + id);
        }
    }

    public int getRoomCount() {
        return rooms.size();
    }
}