/**
 * Implementações concretas para fabricantes específicos
 */
package trabalhofinal.smarthome.devices;

public class SamsungImplementation implements DeviceImplementation {
    @Override
    public String getVendorInfo() {
        return "Samsung Smart Things";
    }

    @Override
    public String performOperation(String operation) {
        return "Samsung device performing: " + operation;
    }
}
