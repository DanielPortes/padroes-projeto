/**
 * Gerenciador de cômodos
 */
package trabalhofinal.smarthome.core;

import trabalhofinal.smarthome.visitor.Room;

import java.util.*;
import java.util.stream.Collectors;

public class RoomManager {
    private final Map<String, Room> rooms;

    public RoomManager() {
        this.rooms = new HashMap<>();
    }

    public trabalhofinal.smarthome.visitor.Room createRoom(String name, String type) {
        trabalhofinal.smarthome.visitor.Room room = new trabalhofinal.smarthome.visitor.Room(name, type);
        rooms.put(room.getId(), room);
        return room;
    }

    public Optional<Room> getRoomById(String id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public Optional<trabalhofinal.smarthome.visitor.Room> getRoomByName(String name) {
        return rooms.values().stream()
                .filter(room -> room.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<trabalhofinal.smarthome.visitor.Room> getRoomsByType(String type) {
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