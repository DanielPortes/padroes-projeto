/**
 * Interface para elementos visitáveis
 */
package trabalhofinal.smarthome.visitor;

public interface Visitable {
    String accept(HomeVisitor visitor);
}
