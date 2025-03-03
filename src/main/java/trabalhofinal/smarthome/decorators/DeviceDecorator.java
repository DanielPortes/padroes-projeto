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
        decoratedDevice.setState(state);
    }

    @Override
    public boolean isActive() {
        return decoratedDevice.isActive();
    }

    @Override
    public String execute(String command) {
        return decoratedDevice.execute(command);
    }

    @Override
    public String getStatus() {
        // Aqui está a correção chave - usar decoratedDevice ao invés de super
        return decoratedDevice.getStatus();
    }
}