/**
 * Interface para subsistemas que usam o mediador
 */
package trabalhofinal.smarthome.mediator;


public interface SubSystem {
    String getName();
    void receiveNotification(String event, Object data);
}

