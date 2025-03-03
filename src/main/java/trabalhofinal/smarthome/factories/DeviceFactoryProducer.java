
/**
 * Factory Method para criar fábricas de dispositivos específicas
 */
package trabalhofinal.smarthome.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DeviceFactoryProducer {
    private static final Map<String, Supplier<DeviceFactory>> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("PHILIPS", PhilipsDeviceFactory::new);
        FACTORIES.put("SAMSUNG", SamsungDeviceFactory::new);
    }

    public static DeviceFactory getFactory(String brand) {
        return Optional.ofNullable(FACTORIES.get(brand.toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported brand: " + brand))
                .get();
    }
}