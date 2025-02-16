package comportamental.exercicio04;

/**
 * Estado Emergency (emergência): o drone encontrou um problema e entra em modo de emergência.
 */
class EmergencyState implements DroneState {
    private static EmergencyState instance = new EmergencyState();
    private EmergencyState() {}
    public static EmergencyState getInstance() { return instance; }

    @Override
    public void takeOff(Drone drone) {
        drone.notifyObservers("Emergência ativa! Não é possível decolar.");
    }

    @Override
    public void land(Drone drone) {
        drone.notifyObservers("Realizando aterrissagem de emergência.");
        drone.setState(IdleState.getInstance());
    }

    @Override
    public void fly(Drone drone) {
        drone.notifyObservers("Emergência ativa! Não é possível voar.");
    }

    @Override
    public void emergency(Drone drone) {
        drone.notifyObservers("Drone já está em estado de emergência.");
    }
}