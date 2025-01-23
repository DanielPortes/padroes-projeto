package padroescriacao.exercicio03;

/**
 * Estado de operação do Tracker.
 */
public class TrackerStateOperation extends TrackerState {

    public TrackerStateOperation(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean handleFault() {
        boolean faultDetected = detectFault();
        if (faultDetected) {
            tracker.setState(stateFactory.createFaultState(tracker));
            return true;
        }
        return false;
    }

    @Override
    public boolean switchToManual() {
        tracker.setState(stateFactory.createManualState(tracker));
        return true;
    }

    @Override
    public boolean switchToAI() {
        tracker.setState(stateFactory.createAIState(tracker));
        return true;
    }

    private boolean detectFault() {
        // Lógica para detectar falhas
        return false;
    }
}
