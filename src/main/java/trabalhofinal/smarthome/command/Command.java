/**
 * Classe base para comandos
 */
package trabalhofinal.smarthome.command;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private final String type;
    private final String target;
    private final Map<String, String> parameters;

    public Command(String type, String target) {
        this.type = type;
        this.target = target;
        this.parameters = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public Command addParameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }

    public Map<String, String> getParameters() {
        return new HashMap<>(parameters);
    }

    @Override
    public String toString() {
        return String.format("Command[type=%s, target=%s, params=%s]",
                type, target, parameters);
    }
}
