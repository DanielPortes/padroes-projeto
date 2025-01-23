package padroescriacao.exercicio03;

/**
 * Estado de Comissionamento do Tracker.
 */
public class TrackerStateCommissioning extends TrackerState {

    public TrackerStateCommissioning(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean commission() {
        boolean commissioningComplete = checkCommissioningComplete();
        if (commissioningComplete) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    private boolean checkCommissioningComplete() {
        // Lógica para verificar se o comissionamento está completo
        return true;
    }
}
