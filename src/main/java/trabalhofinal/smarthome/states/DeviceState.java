/**
 * Interface para estados de dispositivos (State Pattern)
 */
package trabalhofinal.smarthome.states;

import trabalhofinal.smarthome.devices.AbstractDevice;

public interface DeviceState {
    /**
     * Gets the name of this state.
     * @return The state name (e.g., "ON", "OFF", "STANDBY")
     */
    String getName();

    /**
     * Determines if the device is active in this state.
     * @return true if the device is considered active, false otherwise
     */
    boolean isActive();

    /**
     * Handles a command sent to the device.
     * This method should return an appropriate message and update the device's state if needed.
     *
     * @param command The command to handle
     * @return A message describing the result of the command
     */
    String handleCommand(String command);

    /**
     * Updates the state based on any conditions that would trigger an automatic change.
     * Implementations may choose to change the device's state during this method.
     */
    void updateState();
}