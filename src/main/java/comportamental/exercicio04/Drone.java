package comportamental.exercicio04;


import java.util.ArrayList;
import java.util.List;

/**
 * Classe Context (Drone): mantém o estado atual e gerencia os observers.
 */
class Drone {
    private DroneState state;
    private List<DroneObserver> observers = new ArrayList<>();

    public Drone() {
        state = IdleState.getInstance();
        notifyObservers("Estado inicial: Idle");
    }

    public void setState(DroneState state) {
        this.state = state;
        notifyObservers("Transição de estado: " + state.getClass().getSimpleName());
    }

    // Métodos do padrão Observer
    public void addObserver(DroneObserver observer) {
        observers.add(observer);
        if (state == IdleState.getInstance()) {
            observer.update(this, "Estado inicial: Idle");
        } else {
            observer.update(this, "Estado atual: " + state.getClass().getSimpleName());
        }
    }

    public void removeObserver(DroneObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (DroneObserver observer : observers) {
            observer.update(this, message);
        }
    }

    // Métodos que delegam a execução para o estado atual
    public void takeOff() {
        state.takeOff(this);
    }

    public void land() {
        state.land(this);
    }

    public void fly() {
        state.fly(this);
    }

    public void emergency() {
        state.emergency(this);
    }

    // Método auxiliar para testes: retorna o nome da classe do estado atual.
    public String getCurrentState() {
        return state.getClass().getSimpleName();
    }
}