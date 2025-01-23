package padroescriacao.exercicio03;

/**
 * Estado de Inteligência Artificial do Tracker.
 */
public class TrackerStateArtificialIntelligence extends TrackerState {

    public TrackerStateArtificialIntelligence(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean operate() {
        boolean aiTaskComplete = checkAITaskComplete();
        if (aiTaskComplete) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    private boolean checkAITaskComplete() {
        // Lógica para verificar se a tarefa de IA está completa
        return true;
    }
}
