/**
 * Classe que configura a cadeia de responsabilidade
 */
package trabalhofinal.smarthome.command;
//package com.smarthome.command;

public class CommandProcessor {
    private final CommandHandler chain;

    public CommandProcessor() {
        DeviceCommandHandler deviceHandler = new DeviceCommandHandler();
        RoomCommandHandler roomHandler = new RoomCommandHandler();
        SecurityCommandHandler securityHandler = new SecurityCommandHandler();

        // Configurar a cadeia
        chain = deviceHandler;
        deviceHandler.setNext(roomHandler)
                .setNext(securityHandler);
    }

    public String processCommand(Command command) {
        return chain.handleCommand(command);
    }

    public String processCommand(String type, String target, Map<String, String> parameters) {
        Command command = new Command(type, target);
        parameters.forEach(command::addParameter);
        return processCommand(command);
    }
}