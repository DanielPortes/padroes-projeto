/**
 * Interface para estados de dispositivos (State Pattern)
 */
package trabalhofinal.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

public interface DeviceState {
    String getName();
    boolean isActive();
    String handleCommand(String command);
    void updateState();
}
