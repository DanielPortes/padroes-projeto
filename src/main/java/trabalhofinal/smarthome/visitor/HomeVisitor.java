package trabalhofinal.smarthome.visitor;


import trabalhofinal.smarthome.core.Room;
import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;

/**
 * Interface Visitor para o padrão Visitor
 */
public interface HomeVisitor {
    String visitLight(LightDevice light);
    String visitThermostat(ThermostatDevice thermostat);
    String visitRoom(Room room);
}


