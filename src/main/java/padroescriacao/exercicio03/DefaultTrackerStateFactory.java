package padroescriacao.exercicio03;

/**
 * Implementação padrão da TrackerStateFactory que cria instâncias concretas dos estados.
 */
public class DefaultTrackerStateFactory implements TrackerStateFactory {

    @Override
    public TrackerState createStartupState(Tracker tracker) {
        return new TrackerStateStartup(tracker, this);
    }

    @Override
    public TrackerState createOperationState(Tracker tracker) {
        return new TrackerStateOperation(tracker, this);
    }

    @Override
    public TrackerState createFaultState(Tracker tracker) {
        return new TrackerStateFault(tracker, this);
    }

    @Override
    public TrackerState createManualState(Tracker tracker) {
        return new TrackerStateManual(tracker, this);
    }

    @Override
    public TrackerState createAIState(Tracker tracker) {
        return new TrackerStateArtificialIntelligence(tracker, this);
    }

    @Override
    public TrackerState createCommissioningState(Tracker tracker) {
        return new TrackerStateCommissioning(tracker, this);
    }

    @Override
    public TrackerState createConfigurationState(Tracker tracker) {
        return new TrackerStateConfiguration(tracker, this);
    }
}
