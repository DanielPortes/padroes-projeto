package padroescriacao.exercicio03;

/**
 * Estado manual do Tracker.
 */
public class TrackerStateManual extends TrackerState {

    public TrackerStateManual(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean operate() {
        boolean manualComplete = checkManualComplete();
        if (manualComplete) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    private boolean checkManualComplete() {
        // Lógica para verificar se a operação manual está completa
        return true;
    }
}
