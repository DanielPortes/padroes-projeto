package trabalhofinal.smarthome.mediator;

/**
 * Interface Mediator para coordenar comunicações entre subsistemas
 */
public interface SmartHomeMediator {
    void register(SubSystem subSystem);
    void notify(SubSystem sender, String event, Object data);
}
