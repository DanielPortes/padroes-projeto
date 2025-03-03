package trabalhofinal.smarthome.decorators;

import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.states.DeviceState;

/**
 * Decorator base para adicionar funcionalidades aos dispositivos
 */
public abstract class DeviceDecorator extends AbstractDevice {
    protected final AbstractDevice decoratedDevice;

    protected DeviceDecorator(AbstractDevice decoratedDevice) {
        super(decoratedDevice.getName(), decoratedDevice.getImplementation());
        this.decoratedDevice = decoratedDevice;
    }

    @Override
    public String getType() {
        return decoratedDevice.getType();
    }

    @Override
    public DeviceState getState() {
        return decoratedDevice.getState();
    }

    @Override
    public void setState(DeviceState state) {
        // Make sure both this device and the decorated device have the same state
        super.setState(state);
        decoratedDevice.setState(state);
    }

    @Override
    public boolean isActive() {
        // Always rely on the decorated device's state
        return decoratedDevice.isActive();
    }

    @Override
    public String execute(String command) {
        // Execute command on decorated device and return result
        return decoratedDevice.execute(command);
    }

    @Override
    public String getStatus() {
        // Use decorated device's status
        return decoratedDevice.getStatus();
    }
}