
/**
 * Decorator que adiciona recursos de segurança
 */
package trabalhofinal.smarthome.decorators;
import trabalhofinal.smarthome.core.HomeCentral;
import trabalhofinal.smarthome.devices.AbstractDevice;
import trabalhofinal.smarthome.core.HomeCentral;

public class SecurityEnabledDecorator extends DeviceDecorator {
    private boolean securityEnabled;

    public SecurityEnabledDecorator(AbstractDevice decoratedDevice) {
        super(decoratedDevice);
        this.securityEnabled = true;
    }

    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    public void setSecurityEnabled(boolean securityEnabled) {
        this.securityEnabled = securityEnabled;
    }

    @Override
    public String execute(String command) {
        if (securityEnabled && trabalhofinal.smarthome.core.HomeCentral.getInstance().getSecuritySystem().isArmed()) {
            trabalhofinal.smarthome.core.HomeCentral.getInstance().getNotificationCenter()
                    .sendNotification("Security alert: Protected device " +
                            getName() + " received command: " + command);
        }
        return super.execute(command);
    }

    @Override
    public String getStatus() {
        return super.getStatus() + "Security: " + (securityEnabled ? "Enabled" : "Disabled") + "\n";
    }
}