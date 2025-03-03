/**
 * Rotina baseada em eventos
 */
package trabalhofinal.smarthome.automation;
//package com.smarthome.automation;

import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.observer.HomeEvent;
import trabalhofinal.smarthome.strategy.MaxEconomyPowerStrategy;
import trabalhofinal.smarthome.strategy.PowerManager;
import trabalhofinal.smarthome.strategy.PowerManager;

public class SecurityAlertRoutine extends AutomationRoutine {
    public SecurityAlertRoutine() {
        super("Security Alert Response");
    }

    @Override
    protected boolean shouldReactToEvent(HomeEvent event) {
        return "Alert".equals(event.getType()) &&
                event.getDescription().contains("security");
    }

    @Override
    protected void reactToEvent(HomeEvent event) {
        execute();
    }

    @Override
    protected String executeRoutineSteps() {
        StringBuilder result = new StringBuilder();

        // Ativar sistema de segurança
        HomeCentral central = HomeCentral.getInstance();
        central.getSecuritySystem().arm();
        result.append("Security system armed\n");

        // Ativar modo de economia para reduzir consumo
        trabalhofinal.smarthome.strategy.PowerManager powerManager = new trabalhofinal.smarthome.strategy.PowerManager();
        powerManager.setStrategy("Maximum Power Saving");
        result.append(powerManager.applyStrategy(central.getDeviceManager().getAllDevices()));

        // Enviar notificações
        central.getNotificationCenter()
                .sendAlert("Security System", "Security breach detected. Alert mode activated.");
        result.append("\nSecurity alerts sent to registered users");

        return result.toString();
    }
}