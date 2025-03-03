/**
 * Sistema de segurança da casa inteligente
 */
package trabalhofinal.smarthome.core;

import trabalhofinal.smarthome.observer.HomeEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class SecuritySystem {
    private boolean armed;
    private final String securityCode;
    private LocalDateTime lastArmedTime;

    public SecuritySystem() {
        this.armed = false;
        // Em uma implementação real, este código seria configurado de forma segura
        this.securityCode = UUID.randomUUID().toString().substring(0, 8);
    }

    public void arm() {
        armed = true;
        lastArmedTime = LocalDateTime.now();

        // Notificar sistema
        HomeCentral.getInstance().getNotificationCenter()
                .sendAlert("Security System", "System armed at " + lastArmedTime);
    }

    public boolean disarm(String code) {
        if (securityCode.equals(code)) {
            armed = false;

            // Notificar sistema
            HomeCentral.getInstance().getNotificationCenter()
                    .sendNotification("Security system disarmed");

            return true;
        }

        // Registrar tentativa inválida
        HomeCentral.getInstance().getNotificationCenter()
                .sendAlert("Security System", "Invalid disarm attempt!");

        return false;
    }

    public boolean isArmed() {
        return armed;
    }

    public LocalDateTime getLastArmedTime() {
        return lastArmedTime;
    }

    public void triggerAlarm(String source, String reason) {
        if (armed) {
            HomeEvent alarmEvent = new HomeEvent(
                    source,
                    "SECURITY_BREACH",
                    "Security breach detected: " + reason
            );

            HomeCentral.getInstance().getNotificationCenter()
                    .notifyObservers(alarmEvent);
        }
    }
}