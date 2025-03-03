/**
 * Interface para implementações concretas de dispositivos por fabricante
 */
package trabalhofinal.smarthome.devices;

public interface DeviceImplementation {
    String getVendorInfo();
    String performOperation(String operation);
}
