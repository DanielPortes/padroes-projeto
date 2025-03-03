

/**
 * Manipulador para comandos de segurança
 */
package trabalhofinal.smarthome.command;


import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.SecuritySystem;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.core.SecuritySystem;

public class SecurityCommandHandler extends AbstractCommandHandler {
    @Override
    public String handleCommand(Command command) {
        if (command.getType().equals("SECURITY")) {
            trabalhofinal.smarthome.core.SecuritySystem securitySystem = trabalhofinal.smarthome.core.HomeCentral.getInstance().getSecuritySystem();

            String action = command.getParameter("action");
            if (action != null) {
                switch (action.toUpperCase()) {
                    case "ARM":
                        securitySystem.arm();
                        return "Security system armed";
                    case "DISARM":
                        String code = command.getParameter("code");
                        if (code != null) {
                            if (securitySystem.disarm(code)) {
                                return "Security system disarmed";
                            } else {
                                return "Invalid security code";
                            }
                        } else {
                            return "Security code required to disarm system";
                        }
                    default:
                        return "Unknown security action: " + action;
                }
            } else {
                return "Missing action parameter for security command";
            }
        }

        return passToNext(command);
    }
}
