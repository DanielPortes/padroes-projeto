package comportamental.exercicio04;

/**
 * Estado Landing (aterrissando): o drone está em procedimento de pouso.
 */
class LandingState implements DroneState {
    private static LandingState instance = new LandingState();
    private LandingState() {}
    public static LandingState getInstance() { return instance; }

    @Override
    public void takeOff(Drone drone) {
        drone.notifyObservers("Aterrissagem em progresso. Não é possível decolar.");
    }

    @Override
    public void land(Drone drone) {
        drone.notifyObservers("Aterrissagem concluída. Drone no solo.");
        drone.setState(IdleState.getInstance());
    }

    @Override
    public void fly(Drone drone) {
        drone.notifyObservers("Aterrissagem em progresso. Não é possível voar.");
    }

    @Override
    public void emergency(Drone drone) {
        drone.notifyObservers("Emergência durante aterrissagem!");
        drone.setState(EmergencyState.getInstance());
    }
}