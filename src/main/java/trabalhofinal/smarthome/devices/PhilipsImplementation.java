package trabalhofinal.smarthome.devices;

public class PhilipsImplementation implements DeviceImplementation {
    @Override
    public String getVendorInfo() {
        return "Philips Smart Home";
    }

    @Override
    public String performOperation(String operation) {
        return "Philips device performing: " + operation;
    }
}
