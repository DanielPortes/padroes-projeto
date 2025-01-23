package padroescriacao.exercicio03;

/**
 * Estado inicial do Tracker.
 */
public class TrackerStateStartup extends TrackerState {

    public TrackerStateStartup(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean initialize() {
        boolean startupComplete = checkStartupComplete();
        if (startupComplete) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    private boolean checkStartupComplete() {
        // Lógica para verificar se o startup está completo
        return true;
    }
}
