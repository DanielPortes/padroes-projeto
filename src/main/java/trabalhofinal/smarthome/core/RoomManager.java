/**
 * Gerenciador de cômodos
 */
package trabalhofinal.smarthome.core;

import java.util.*;
import java.util.stream.Collectors;

public class RoomManager {
    private final Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    public Room createRoom(String name, String type) {
        Room room = new Room(name, type);
        rooms.put(room.getId(), room);
        return room;
    }

    public Optional<Room> getRoomById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public Optional<Room> getRoomByName(String name) {
        return rooms.values().stream()
                .filter(room -> room.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> getRoomsByType(String type) {
        return rooms.values().stream()
                .filter(room -> room.getAttributes().getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public void removeRoom(String id) {
        rooms.remove(id);
    }

    public int getRoomCount() {
        return rooms.size();
    }
}