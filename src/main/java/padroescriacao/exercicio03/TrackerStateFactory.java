package padroescriacao.exercicio03;

/**
 * Interface responsável por criar instâncias dos diferentes estados do Tracker.
 */
public interface TrackerStateFactory {
    TrackerState createStartupState(Tracker tracker);
    TrackerState createOperationState(Tracker tracker);
    TrackerState createFaultState(Tracker tracker);
    TrackerState createManualState(Tracker tracker);
    TrackerState createAIState(Tracker tracker);
    TrackerState createCommissioningState(Tracker tracker);
    TrackerState createConfigurationState(Tracker tracker);
    // Adicione métodos para outros estados conforme necessário
}
