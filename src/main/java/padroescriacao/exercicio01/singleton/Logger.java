package padroescriacao.exercicio01.singleton;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static volatile Logger instance;
    private final List<String> logs;

    private Logger() {
        this.logs = new ArrayList<>();
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        logs.add("[LOG]: " + message);
    }

    public List<String> getLogs() {
        return logs;
    }
}
