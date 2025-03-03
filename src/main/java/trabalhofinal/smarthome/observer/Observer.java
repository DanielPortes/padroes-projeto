/**
 * Interface para classes observadoras (Subscriber)
 */
package trabalhofinal.smarthome.observer;

public interface Observer<T> {
    void update(T event);
}
