/**
 * Implementação do padrão Singleton para o controlador central do sistema.
 * Esta classe gerencia todos os subsistemas da casa inteligente.
 */
package trabalhofinal.smarthome.core;

import java.util.logging.Logger;

public class HomeCentral {
    private static final Logger LOGGER = Logger.getLogger(HomeCentral.class.getName());
    private static volatile HomeCentral instance;
    private final DeviceManager deviceManager;
    private final RoomManager roomManager;
    private final SecuritySystem securitySystem;
    private final NotificationCenter notificationCenter;

    private HomeCentral() {
        deviceManager = new DeviceManager();
        roomManager = new RoomManager();
        securitySystem = new SecuritySystem();
        notificationCenter = new NotificationCenter();
        LOGGER.info("Home Central System initialized");
    }

    public static HomeCentral getInstance() {
        if (instance == null) {
            synchronized (HomeCentral.class) {
                if (instance == null) {
                    instance = new HomeCentral();
                }
            }
        }
        return instance;
    }

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public SecuritySystem getSecuritySystem() {
        return securitySystem;
    }

    public NotificationCenter getNotificationCenter() {
        return notificationCenter;
    }

    public String getSystemStatus() {
        return String.format(
                "Smart Home System Status:\n" +
                        "Devices: %d active\n" +
                        "Rooms: %d configured\n" +
                        "Security: %s\n",
                deviceManager.getActiveDeviceCount(),
                roomManager.getRoomCount(),
                securitySystem.isArmed() ? "Armed" : "Disarmed"
        );
    }
}