package trabalhofinal.smarthome.core;


import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.flyweight.RoomAttributes;
import trabalhofinal.smarthome.flyweight.RoomAttributesFactory;
import trabalhofinal.smarthome.visitor.Visitable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe que representa um cômodo da casa
 */
public class Room  implements Visitable {
    private final String id;
    private final String name;
    private final RoomAttributes attributes;
    private final List<AbstractDevice> devices;

    public Room(String name, String type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        // Usando o Flyweight para atributos compartilhados
        this.attributes = RoomAttributesFactory.getRoomAttributes(type);
        this.devices = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoomAttributes getAttributes() {
        return attributes;
    }

    public void addDevice(AbstractDevice device) {
        devices.add(device);
    }

    public void removeDevice(AbstractDevice device) {
        devices.remove(device);
    }

    public List<AbstractDevice> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    public Optional<AbstractDevice> getDeviceByName(String deviceName) {
        return devices.stream()
                .filter(device -> device.getName().equalsIgnoreCase(deviceName))
                .findFirst();
    }

    public String turnAllDevicesOn() {
        return devices.stream()
                .map(device -> device.execute("ON"))
                .collect(Collectors.joining("\n"));
    }

    public String turnAllDevicesOff() {
        return devices.stream()
                .map(device -> device.execute("OFF"))
                .collect(Collectors.joining("\n"));
    }

    public long getActiveDeviceCount() {
        return devices.stream().filter(AbstractDevice::isActive).count();
    }

    public String getRoomStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Room: ").append(name).append(" (").append(attributes.getType()).append(")\n");
        status.append("Active Devices: ").append(getActiveDeviceCount()).append("/").append(devices.size()).append("\n");
        status.append("Default Settings: Light: ").append(attributes.getDefaultLightLevel());
        status.append(", Temperature: ").append(attributes.getDefaultTemperature()).append("\n");

        devices.forEach(device -> {
            status.append("- ").append(device.getName()).append(": ");
            status.append(device.isActive() ? "Active" : "Inactive").append("\n");
        });

        return status.toString();
    }

    @Override
    public String accept(trabalhofinal.smarthome.visitor.HomeVisitor visitor) {
        return visitor.visitRoom(this);
    }
}

