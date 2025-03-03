/**
 * Classe que configura a cadeia de responsabilidade
 */
package trabalhofinal.smarthome.command;

import java.util.Map;
import java.util.logging.Logger;

public class CommandProcessor {
    private static final Logger LOGGER = Logger.getLogger(CommandProcessor.class.getName());
    private final CommandHandler chain;

    public CommandProcessor() {
        DeviceCommandHandler deviceHandler = new DeviceCommandHandler();
        RoomCommandHandler roomHandler = new RoomCommandHandler();
        SecurityCommandHandler securityHandler = new SecurityCommandHandler();

        // Configurar a cadeia
        chain = deviceHandler;
        deviceHandler.setNext(roomHandler)
                .setNext(securityHandler);

        LOGGER.fine("Command processor chain initialized");
    }

    public String processCommand(Command command) {
        LOGGER.fine("Processing command: " + command);
        String result = chain.handleCommand(command);
        LOGGER.fine("Command result: " + result);
        return result;
    }

    public String processCommand(String type, String target, Map<String, String> parameters) {
        LOGGER.fine("Creating command of type: " + type + ", target: " + target);
        Command command = new Command(type, target);
        parameters.forEach((key, value) -> {
            LOGGER.fine("Adding parameter: " + key + "=" + value);
            command.addParameter(key, value);
        });

        return processCommand(command);
    }
}