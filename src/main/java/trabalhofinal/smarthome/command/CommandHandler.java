/**
 * Interface para manipuladores na cadeia de responsabilidade
 */
package trabalhofinal.smarthome.command;



public interface CommandHandler {
    String handleCommand(Command command);
    CommandHandler setNext(CommandHandler next);
}

/**
 * Manipulador base abstrato
 */
//package trabalhofinal.smarthome.command;

