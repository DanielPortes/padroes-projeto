package trabalhofinal.smarthome.command;

public abstract class AbstractCommandHandler implements CommandHandler {
    private CommandHandler nextHandler;

    @Override
    public CommandHandler setNext(CommandHandler next) {
        this.nextHandler = next;
        return next;
    }

    protected String passToNext(Command command) {
        if (nextHandler != null) {
            return nextHandler.handleCommand(command);
        }
        return "End of chain reached. Command not handled: " + command.getType();
    }
}
