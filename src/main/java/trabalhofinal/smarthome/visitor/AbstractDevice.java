/**
 * Adaptação dos dispositivos existentes para suportar o padrão Visitor
 */
package trabalhofinal.smarthome.visitor;
//package com.smarthome.devices;

import trabalhofinal.smarthome.devices.LightDevice;
import trabalhofinal.smarthome.devices.ThermostatDevice;
import trabalhofinal.smarthome.visitor.HomeVisitor;
import trabalhofinal.smarthome.visitor.Visitable;

public abstract class AbstractDevice implements trabalhofinal.smarthome.visitor.Visitable {
    // Todos os atributos e métodos existentes continuam igual

    // Implementação do método accept para o padrão Visitor
    @Override
    public String accept(trabalhofinal.smarthome.visitor.HomeVisitor visitor) {
        if (this instanceof LightDevice) {
            return visitor.visitLight((LightDevice) this);
        } else if (this instanceof ThermostatDevice) {
            return visitor.visitThermostat((ThermostatDevice) this);
        }
        return "Unknown device type";
    }
}
