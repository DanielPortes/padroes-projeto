package trabalhofinal.smarthome.core;

import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.flyweight.RoomAttributes;
import trabalhofinal.smarthome.flyweight.RoomAttributesFactory;
import trabalhofinal.smarthome.visitor.Visitable;

import java.util.*;
import java.util.logging.Logger;

/**
 * Classe que representa um cômodo da casa
 */
public class Room implements Visitable {
    private static final Logger LOGGER = Logger.getLogger(Room.class.getName());

    private final String id;
    private final String name;
    private final RoomAttributes attributes;
    private final List<AbstractDevice> devices;

    public Room(String name, String type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attributes = RoomAttributesFactory.getRoomAttributes(type);
        this.devices = new ArrayList<>();
        LOGGER.fine("Created room: " + name + " (" + type + ")");
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
        LOGGER.fine("Added device to room " + name + ": " + device.getName());
    }

    public void removeDevice(AbstractDevice device) {
        devices.remove(device);
        LOGGER.fine("Removed device from room " + name + ": " + device.getName());
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
        LOGGER.info("Turning on all devices in room: " + name + " (device count: " + devices.size() + ")");
        StringBuilder results = new StringBuilder();

        if (devices.isEmpty()) {
            String msg = "No devices in room " + name;
            LOGGER.fine(msg);
            return msg;
        }

        for (AbstractDevice device : devices) {
            try {
                LOGGER.info("Turning ON device: " + device.getName() + " (initial state: " + device.isActive() + ")");

                // Execute the command
                String result = device.execute("ON");
                results.append(device.getName()).append(": ").append(result).append("\n");

                // Verify device is now active
                if (!device.isActive()) {
                    LOGGER.warning("Device " + device.getName() + " failed to activate, retrying");
                    // Try again with direct execute
                    device.execute("ON");

                    // Check again
                    if (!device.isActive()) {
                        LOGGER.severe("Device " + device.getName() + " could not be activated even after retry");
                    } else {
                        LOGGER.fine("Device " + device.getName() + " activated after retry");
                    }
                } else {
                    LOGGER.fine("Device " + device.getName() + " successfully activated");
                }
            } catch (Exception e) {
                LOGGER.severe("Error activating device " + device.getName() + ": " + e.getMessage());
                results.append(device.getName()).append(": Error - ").append(e.getMessage()).append("\n");
            }
        }

        // Log summary
        long activeCount = getActiveDeviceCount();
        LOGGER.fine("Room " + name + " activation complete. Active devices: " + activeCount + "/" + devices.size());

        return results.toString();
    }

    public String turnAllDevicesOff() {
        LOGGER.info("Turning off all devices in room: " + name + " (device count: " + devices.size() + ")");
        StringBuilder results = new StringBuilder();

        if (devices.isEmpty()) {
            String msg = "No devices in room " + name;
            LOGGER.info(msg);
            return msg;
        }

        // Log initial device states
        for (AbstractDevice device : devices) {
            LOGGER.info("Initial state of " + device.getName() + ": " + (device.isActive() ? "ON" : "OFF"));
        }

        for (AbstractDevice device : devices) {
            try {
                LOGGER.info("Turning OFF device: " + device.getName());
                String result = device.execute("OFF");
                results.append(device.getName()).append(": ").append(result).append("\n");

                // Verify device is now inactive
                if (device.isActive()) {
                    LOGGER.warning("Device " + device.getName() + " failed to deactivate, retrying");
                    device.execute("OFF");

                    // Check again
                    if (device.isActive()) {
                        LOGGER.severe("Device " + device.getName() + " could not be deactivated even after retry");
                    } else {
                        LOGGER.info("Device " + device.getName() + " deactivated after retry");
                    }
                } else {
                    LOGGER.info("Device " + device.getName() + " successfully deactivated");
                }
            } catch (Exception e) {
                LOGGER.severe("Error deactivating device " + device.getName() + ": " + e.getMessage());
                results.append(device.getName()).append(": Error - ").append(e.getMessage()).append("\n");
            }
        }

        // Log final device states
        for (AbstractDevice device : devices) {
            LOGGER.info("Final state of " + device.getName() + ": " + (device.isActive() ? "ON" : "OFF"));
        }

        return results.toString();
    }

    public long getActiveDeviceCount() {
        long count = devices.stream().filter(AbstractDevice::isActive).count();
        LOGGER.fine("Room " + name + " active device count: " + count + "/" + devices.size());
        return count;
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