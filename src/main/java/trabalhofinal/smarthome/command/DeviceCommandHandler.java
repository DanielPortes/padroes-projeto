
/**
 * Manipulador para comandos de dispositivos
 */
package trabalhofinal.smarthome.command;

import trabalhofinal.smarthome.devices.AbstractDevice;

import java.util.Optional;

public class DeviceCommandHandler extends AbstractCommandHandler {
    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("DEVICE")) {
            trabalhofinal.smarthome.core.DeviceManager deviceManager = trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager();
            Optional<AbstractDevice> device = deviceManager.getDeviceByName(command.getTarget());

            if (device.isPresent()) {
                String action = command.getParameter("action");
                if (action != null) {
                    // Executar o comando no dispositivo e garantir que o estado seja atualizado
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

