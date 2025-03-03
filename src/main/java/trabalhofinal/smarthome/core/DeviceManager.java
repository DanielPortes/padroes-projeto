package trabalhofinal.smarthome.core;

//package com.smarthome.core;

import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.observer.HomeEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gerenciador de dispositivos do sistema
 */
public class DeviceManager {
    private final Map<String, AbstractDevice> devices;

    public DeviceManager() {
        this.devices = new HashMap<>();
    }

    public void registerDevice(AbstractDevice device) {
        devices.put(device.getId(), device);
    }

    public void unregisterDevice(String id) {
        devices.remove(id);
    }

    public Optional<AbstractDevice> getDeviceById(String id) {
        return Optional.ofNullable(devices.get(id));
    }

    public Optional<AbstractDevice> getDeviceByName(String name) {
        return devices.values().stream()
                .filter(device -> device.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<AbstractDevice> getAllDevices() {
        return new ArrayList<>(devices.values());
    }

    public List<AbstractDevice> getDevicesByType(String type) {
        return devices.values().stream()
                .filter(device -> device.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<AbstractDevice> getActiveDevices() {
        return devices.values().stream()
                .filter(AbstractDevice::isActive)
                .collect(Collectors.toList());
    }

    public int getActiveDeviceCount() {
        return (int) devices.values().stream()
                .filter(AbstractDevice::isActive)
                .count();
    }

    public String executeCommandOnAllDevices(String command) {
        return devices.values().stream()
                .map(device -> device.getName() + ": " + device.execute(command))
                .collect(Collectors.joining("\n"));
    }
}

