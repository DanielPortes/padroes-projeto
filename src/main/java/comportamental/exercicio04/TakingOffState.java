package comportamental.exercicio04;

/*
 * Estado TakingOff (decolando): o drone está iniciando a decolagem.
 */
class TakingOffState implements DroneState {
    private static TakingOffState instance = new TakingOffState();
    private TakingOffState() {}
    public static TakingOffState getInstance() { return instance; }

    @Override
    public void takeOff(Drone drone) {
        drone.notifyObservers("Decolagem já em andamento.");
    }

    @Override
    public void land(Drone drone) {
        drone.notifyObservers("Não é possível aterrissar durante a decolagem.");
    }

    @Override
    public void fly(Drone drone) {
        drone.notifyObservers("Decolagem concluída. Drone agora em voo.");
        drone.setState(InFlightState.getInstance());
    }

    @Override
    public void emergency(Drone drone) {
        drone.notifyObservers("Emergência detectada durante a decolagem!");
        drone.setState(EmergencyState.getInstance());
    }
}