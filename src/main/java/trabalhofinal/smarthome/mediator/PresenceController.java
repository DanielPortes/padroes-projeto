/**
 * Exemplo de Subsistema: Controlador de Presença
 */
package trabalhofinal.smarthome.mediator;

import java.util.logging.Logger;

public class PresenceController implements SubSystem {
    private static final Logger LOGGER = Logger.getLogger(PresenceController.class.getName());
    private final SmartHomeMediator mediator;
    private boolean someoneHome;

    public PresenceController(SmartHomeMediator mediator) {
        this.mediator = mediator;
        this.someoneHome = false;
        mediator.register(this);
    }

    @Override
    public String getName() {
        return "Presence Controller";
    }

    @Override
    public void receiveNotification(String event, Object data) {
        switch (event) {
            case "SECURITY_BREACH":
                LOGGER.info("Presence controller detected security breach, checking if anyone is home");
                if (!someoneHome) {
                    mediator.notify(this, "UNEXPECTED_PRESENCE", "Security breach while no one home");
                }
                break;

            case "POWER_SAVING_MODE":
                LOGGER.info("Power saving mode change: " + data);
                break;
        }
    }

    public void userArrived() {
        someoneHome = true;
        LOGGER.info("User arrived home");
        mediator.notify(this, "USER_HOME", null);
    }

    public void userLeft() {
        someoneHome = false;
        LOGGER.info("User left home");
        mediator.notify(this, "USER_AWAY", null);
    }

    public boolean isSomeoneHome() {
        return someoneHome;
    }
}
