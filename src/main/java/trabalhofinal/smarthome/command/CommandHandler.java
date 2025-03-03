/**
 * Interface para manipuladores na cadeia de responsabilidade
 */
package trabalhofinal.smarthome.command;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.DeviceManager;
import trabalhofinal.smarthome.core.RoomManager;
import trabalhofinal.smarthome.core.SecuritySystem;
import trabalhofinal.smarthome.visitor.AbstractDevice;
import trabalhofinal.smarthome.visitor.Room;
//package com.smarthome.command;


public interface CommandHandler {
    String handleCommand(Command command);
    CommandHandler setNext(CommandHandler next);
}

/**
 * Manipulador base abstrato
 */
//package com.smarthome.command;

