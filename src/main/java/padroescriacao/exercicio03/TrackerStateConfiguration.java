package padroescriacao.exercicio03;

/**
 * Estado de Configuração do Tracker.
 */
public class TrackerStateConfiguration extends TrackerState {

    public TrackerStateConfiguration(Tracker tracker, TrackerStateFactory stateFactory) {
        super(tracker, stateFactory);
    }

    @Override
    public boolean configure() {
        boolean configurationComplete = checkConfigurationComplete();
        if (configurationComplete) {
            tracker.setState(stateFactory.createOperationState(tracker));
            return true;
        }
        return false;
    }

    private boolean checkConfigurationComplete() {
        // Lógica para verificar se a configuração está completa
        return true;
    }
}
