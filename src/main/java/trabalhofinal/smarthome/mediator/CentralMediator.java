/**
 * Implementação concreta do mediador
 */
package trabalhofinal.smarthome.mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CentralMediator implements SmartHomeMediator {
    private static final Logger LOGGER = Logger.getLogger(CentralMediator.class.getName());
    private final List<SubSystem> registeredSystems = new ArrayList<>();

    @Override
    public void register(SubSystem subSystem) {
        registeredSystems.add(subSystem);
        LOGGER.info("Registered subsystem: " + subSystem.getName());
    }

    @Override
    public void notify(SubSystem sender, String event, Object data) {
        LOGGER.info(String.format("Event '%s' from '%s'", event, sender.getName()));

        registeredSystems.stream()
                .filter(system -> system != sender)
                .forEach(system -> system.receiveNotification(event, data));
    }
}
