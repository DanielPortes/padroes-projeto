
/**
 * Manipulador para comandos de dispositivos
 */
package trabalhofinal.smarthome.command;
//package com.smarthome.command;

//import com.smarthome.core.DeviceManager;
//import com.smarthome.core.HomeCentral;
//import com.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.core.DeviceManager;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.visitor.AbstractDevice;

import java.util.Optional;

public class DeviceCommandHandler extends AbstractCommandHandler {
    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("DEVICE")) {
            trabalhofinal.smarthome.core.DeviceManager deviceManager = trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager();
            Optional<trabalhofinal.smarthome.visitor.AbstractDevice> device = deviceManager.getDeviceByName(command.getTarget());

            if (device.isPresent()) {
                String action = command.getParameter("action");
                if (action != null) {
                    return device.get().execute(action);
                } else {
                    return "Missing action parameter for device command";
                }
            } else {
                return "Device not found: " + command.getTarget();
            }
        }

        return passToNext(command);
    }
}


