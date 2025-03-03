/**
 * Implementação concreta de dispositivo de iluminação
 */
package trabalhofinal.smarthome.devices;

public class LightDevice extends AbstractDevice {
    private int brightness;

    public LightDevice(String name, DeviceImplementation implementation) {
        super(name, implementation);
        this.brightness = 0;
    }

    @Override
    public String getType() {
        return "Light";
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = Math.max(0, Math.min(100, brightness));
    }

    @Override
    public String getStatus() {
        return super.getStatus() + "Brightness: " + brightness + "%\n";
    }
}
