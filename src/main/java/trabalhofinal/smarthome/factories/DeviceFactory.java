package trabalhofinal.smarthome.factories;

//import com.smarthome.devices.LightDevice;
//import com.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.PhilipsImplementation;
import trabalhofinal.smarthome.devices.SamsungImplementation;
import trabalhofinal.smarthome.devices.ThermostatDevice;

/**
 * Interface para fábricas de dispositivos (Abstract Factory)
 */
public interface DeviceFactory {
    LightDevice createLight(String name);
    ThermostatDevice createThermostat(String name);
}





