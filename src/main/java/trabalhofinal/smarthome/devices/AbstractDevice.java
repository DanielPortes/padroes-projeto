package trabalhofinal.smarthome.devices;

import trabalhofinal.smarthome.states.DeviceState;
import trabalhofinal.smarthome.states.OffState;
import trabalhofinal.smarthome.states.OnState;
import trabalhofinal.smarthome.visitor.Visitable;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementação do padrão Bridge para separar abstração de dispositivos
 * de suas implementações concretas de fabricantes específicos.
 */
public abstract class AbstractDevice implements Visitable {
    private static final Logger LOGGER = Logger.getLogger(AbstractDevice.class.getName());

    private final String id;
    private final String name;
    private final DeviceImplementation implementation;
    private DeviceState state;

    protected AbstractDevice(String name, DeviceImplementation implementation) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.implementation = implementation;
        this.state = new OffState(this);
        LOGGER.info("Created device: " + name + " (id: " + id + ")");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract String getType();

    public DeviceImplementation getImplementation() {
        return implementation;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        LOGGER.info("Device " + name + " state changing from " + this.state.getName() + " to " + state.getName());
        this.state = state;
    }

    public boolean isActive() {
        boolean active = state.isActive();
        LOGGER.fine("Device " + name + " isActive() = " + active + " (state: " + state.getName() + ")");
        return active;
    }

    public String execute(String command) {
        LOGGER.info("Executing command '" + command + "' on device: " + name + " (current state: " + state.getName() + ")");

        String result;

        // Handle critical commands directly
        if (command.equalsIgnoreCase("ON") && !isActive()) {
            LOGGER.info("Direct ON command for device: " + name);
            this.state = new OnState(this);
            result = "Device turned on";
        } else if (command.equalsIgnoreCase("OFF") && isActive()) {
            LOGGER.info("Direct OFF command for device: " + name);
            this.state = new OffState(this);
            result = "Device turned off";
        } else {
            // Let state handle other commands
            result = state.handleCommand(command);
        }

        LOGGER.info("Command result: " + result + " | New state: " + state.getName() + " | Active: " + isActive());
        return result;
    }

    public String getStatus() {
        return String.format(
                "Device: %s (Type: %s)\n" +
                        "Status: %s\n" +
                        "Vendor: %s\n",
                name,
                getType(),
                state.getName(),
                implementation.getVendorInfo()
        );
    }

    @Override
    public String accept(trabalhofinal.smarthome.visitor.HomeVisitor visitor) {
        if (this instanceof LightDevice) {
            return visitor.visitLight((LightDevice) this);
        } else if (this instanceof ThermostatDevice) {
            return visitor.visitThermostat((ThermostatDevice) this);
        }
        return "Unknown device type";
    }
}