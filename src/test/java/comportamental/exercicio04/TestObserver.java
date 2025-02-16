package comportamental.exercicio04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Observer de teste que coleta as mensagens notificadas.
 */
class TestObserver implements DroneObserver {
    private List<String> messages = new ArrayList<>();

    @Override
    public String update(Drone drone, String message) {
        messages.add(message);
        return message;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void clear() {
        messages.clear();
    }
}
