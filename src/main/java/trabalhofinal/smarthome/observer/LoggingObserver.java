/**
 * Implementação do Centro de Notificações usando o padrão Observer
 */
package trabalhofinal.smarthome.observer;

import java.util.logging.Logger;

public class LoggingObserver implements Observer<HomeEvent> {
    private static final Logger LOGGER = Logger.getLogger(LoggingObserver.class.getName());

    @Override
    public void update(HomeEvent event) {
        LOGGER.info(event.toString());
    }
}
