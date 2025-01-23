package padroescriacao.exercicio03;

/**
 * Estado de falha do Tracker.
 */
public class TrackerStateFault extends TrackerState {

    public TrackerStateFault(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean handleFault() {
        boolean faultResolved = resolveFault();
        if (faultResolved) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    @Override
    public boolean switchToManual() {
        tracker.setState(stateFactory.createManualState(tracker));
        return true;
    }

    private boolean resolveFault() {
        // Lógica para resolver a falha
        return true;
    }
}
