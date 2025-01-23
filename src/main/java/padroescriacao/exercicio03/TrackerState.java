package padroescriacao.exercicio03;

/**
 * Classe abstrata que representa o estado do Tracker.
 */
public abstract class TrackerState {
    protected Tracker tracker;
    protected TrackerStateFactory stateFactory;

    public TrackerState(Tracker tracker, TrackerStateFactory stateFactory) {
        this.tracker = tracker;
        this.stateFactory = stateFactory;
    }

    public boolean initialize() {
        return false;
    }

    public boolean commission() {
        return false;
    }

    public boolean operate() {
        return false;
    }

    public boolean configure() {
        return false;
    }

    public boolean handleFault() {
        return false;
    }

    public boolean switchToManual() {
        return false;
    }

    public boolean switchToAI() {
        return false;
    }

    public String getStateName() {
        return this.getClass().getSimpleName();
    }
}
