/**
 * Exemplo de Subsistema: Controlador de Energia
 */
package trabalhofinal.smarthome.mediator;

import java.util.logging.Logger;

public class PowerController implements SubSystem {
    private static final Logger LOGGER = Logger.getLogger(PowerController.class.getName());
    private final SmartHomeMediator mediator;
    private final trabalhofinal.smarthome.strategy.PowerManager powerManager;

    public PowerController(SmartHomeMediator mediator) {
        this.mediator = mediator;
        this.powerManager = new trabalhofinal.smarthome.strategy.PowerManager();
        mediator.register(this);
    }

    @Override
    public String getName() {
        return "Power Controller";
    }

    @Override
    public void receiveNotification(String event, Object data) {
        switch (event) {
            case "SECURITY_BREACH":
                LOGGER.info("Power controller responding to security breach");
                powerManager.setStrategy("Maximum Power Saving");
                powerManager.applyStrategy(trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager().getAllDevices());
                mediator.notify(this, "POWER_SAVING_MODE", "ENABLED");
                break;

            case "USER_AWAY":
                LOGGER.info("Power controller responding to user away");
                powerManager.setStrategy("Balanced Power Saving");
                powerManager.applyStrategy(trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager().getAllDevices());
                mediator.notify(this, "POWER_SAVING_MODE", "BALANCED");
                break;

            case "USER_HOME":
                LOGGER.info("Power controller responding to user home");
                // Nome corrigido da estratégia
                powerManager.setStrategy("Comfort Mode");
                powerManager.applyStrategy(trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager().getAllDevices());
                mediator.notify(this, "POWER_SAVING_MODE", "DISABLED");
                break;
        }
    }
    public void reportExcessivePowerUsage() {
        LOGGER.warning("Excessive power usage detected");
        mediator.notify(this, "EXCESSIVE_POWER", trabalhofinal.smarthome.core.HomeCentral.getInstance().getDeviceManager().getActiveDeviceCount());
    }
}

