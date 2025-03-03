package trabalhofinal.smarthome.observer;


/**
 * Interface para classes observáveis (Publisher)
 */
public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void notifyObservers(T event);
}

