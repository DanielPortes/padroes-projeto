package trabalhofinal.smarthome.devices;


import trabalhofinal.smarthome.states.DeviceState;
import trabalhofinal.smarthome.states.OffState;
import trabalhofinal.smarthome.visitor.Visitable;

import java.util.UUID;

/**
 * Implementação do padrão Bridge para separar abstração de dispositivos
 * de suas implementações concretas de fabricantes específicos.
 */
public abstract class AbstractDevice implements Visitable {
    private final String id;
    private final String name;
    private final DeviceImplementation implementation;
    private DeviceState state;

    protected AbstractDevice(String name, DeviceImplementation implementation) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.implementation = implementation;
        this.state = new OffState(this);
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
        this.state = state;
    }

    public boolean isActive() {
        return state.isActive();
    }

    public String execute(String command) {
        String result = state.handleCommand(command);
        // Atualizar estado após a execução do comando
        state.updateState();
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
