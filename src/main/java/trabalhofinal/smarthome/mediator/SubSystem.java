/**
 * Interface para subsistemas que usam o mediador
 */
package trabalhofinal.smarthome.mediator;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.strategy.PowerManager;

public interface SubSystem {
    String getName();
    void receiveNotification(String event, Object data);
}

