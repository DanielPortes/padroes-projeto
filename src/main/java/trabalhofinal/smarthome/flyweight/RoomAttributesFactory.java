/**
 * Fábrica Flyweight para gerenciar atributos compartilhados
 */
package trabalhofinal.smarthome.flyweight;

import java.util.HashMap;
import java.util.Map;

public class RoomAttributesFactory {
    private static final Map<String, RoomAttributes> roomTypes = new HashMap<>();

    public static RoomAttributes getRoomAttributes(String type) {
        return roomTypes.computeIfAbsent(type, RoomAttributesFactory::createRoomAttributes);
    }

    private static RoomAttributes createRoomAttributes(String type) {
        switch (type.toLowerCase()) {
            case "living room":
                return new RoomAttributes.Builder(type)
                        .defaultLightLevel("60%")
                        .defaultTemperature("22C")
                        .wallColor("Beige")
                        .floorType("Hardwood")
                        .build();
            case "bedroom":
                return new RoomAttributes.Builder(type)
                        .defaultLightLevel("40%")
                        .defaultTemperature("20C")
                        .wallColor("Light Blue")
                        .floorType("Carpet")
                        .build();
            case "kitchen":
                return new RoomAttributes.Builder(type)
                        .defaultLightLevel("80%")
                        .defaultTemperature("23C")
                        .wallColor("White")
                        .floorType("Tile")
                        .build();
            case "bathroom":
                return new RoomAttributes.Builder(type)
                        .defaultLightLevel("70%")
                        .defaultTemperature("24C")
                        .wallColor("Light Green")
                        .floorType("Tile")
                        .build();
            case "office":
                return new RoomAttributes.Builder(type)
                        .defaultLightLevel("65%")
                        .defaultTemperature("21C")
                        .wallColor("Gray")
                        .floorType("Hardwood")
                        .build();
            default:
                return new RoomAttributes.Builder(type).build();
        }
    }

    public static int getCacheSize() {
        return roomTypes.size();
    }

    public static void clearCache() {
        roomTypes.clear();
    }
}
