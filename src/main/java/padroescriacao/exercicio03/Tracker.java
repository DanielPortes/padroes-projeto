package padroescriacao.exercicio03;

/**
 * Classe principal que gerencia os estados do Tracker.
 */
public class Tracker {
    private TrackerState currentState;
    private final TrackerStateFactory stateFactory;

    /**
     * Construtor que recebe a fábrica de estados através da injeção de dependência.
     * @param stateFactory Fábrica para criação de estados.
     */
    public Tracker(TrackerStateFactory stateFactory) {
        this.stateFactory = stateFactory;
        this.currentState = stateFactory.createStartupState(this);
    }

    /**
     * Método para atualizar o estado atual do Tracker.
     * @param newState Novo estado a ser setado.
     */
    public void setState(TrackerState newState) {
        this.currentState = newState;
    }

    public boolean initialize() {
        return this.currentState.initialize();
    }

    public boolean commission() {
        return this.currentState.commission();
    }

    public boolean operate() {
        return this.currentState.operate();
    }

    public boolean configure() {
        return this.currentState.configure();
    }

    public boolean handleFault() {
        return this.currentState.handleFault();
    }

    public boolean switchToManual() {
        return this.currentState.switchToManual();
    }

    public boolean switchToAI() {
        return this.currentState.switchToAI();
    }

    public String getState() {
        return currentState.getStateName();
    }
}
