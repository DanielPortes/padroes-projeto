/**
 * Adaptação da classe Room para suportar o padrão Visitor
 */
package trabalhofinal.smarthome.visitor;
//package trabalhofinal.smarthome.core;

import trabalhofinal.smarthome.visitor.HomeVisitor;
import trabalhofinal.smarthome.visitor.Visitable;

public class Room implements trabalhofinal.smarthome.visitor.Visitable {
    // Todos os atributos e métodos existentes continuam igual

    // Implementação do método accept para o padrão Visitor
    @Override
    public String accept(trabalhofinal.smarthome.visitor.HomeVisitor visitor) {
        return visitor.visitRoom(this);
    }
}
