/**
 * Classe de evento base para o sistema
 */
package trabalhofinal.smarthome.observer;

import java.time.LocalDateTime;

public class HomeEvent {
    private final String source;
    private final String type;
    private final String description;
    private final LocalDateTime timestamp;

    public HomeEvent(String source, String type, String description) {
        this.source = source;
        this.type = type;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s - %s",
                timestamp, type, source, description);
    }
}
