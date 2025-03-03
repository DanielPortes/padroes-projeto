/**
 * Exemplos de observers concretos
 */
package trabalhofinal.smarthome.observer;

import java.util.function.Consumer;

public class MobileAppNotifier implements Observer<HomeEvent> {
    private final String userId;
    private final Consumer<String> notificationSender;

    public MobileAppNotifier(String userId, Consumer<String> notificationSender) {
        this.userId = userId;
        this.notificationSender = notificationSender;
    }

    @Override
    public void update(HomeEvent event) {
        if (shouldNotifyUser(event)) {
            String message = formatMessage(event);
            notificationSender.accept(message);
        }
    }

    private boolean shouldNotifyUser(HomeEvent event) {
        // Lógica para determinar se o usuário deve ser notificado deste evento
        return event.getType().equals("Alert") ||
                event.getDescription().contains("critical");
    }

    private String formatMessage(HomeEvent event) {
        return String.format("SmartHome Notification: %s", event.getDescription());
    }

    public String getUserId() {
        return userId;
    }
}
