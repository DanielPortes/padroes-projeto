package comportamental.exercicio04;

/*
 * Estado InFlight (em voo): o drone está voando normalmente.
 */
class InFlightState implements DroneState {
    private static InFlightState instance = new InFlightState();
    private InFlightState() {}
    public static InFlightState getInstance() { return instance; }

    @Override
    public void takeOff(Drone drone) {
        drone.notifyObservers("Drone já está em voo.");
    }

    @Override
    public void land(Drone drone) {
        drone.notifyObservers("Iniciando procedimento de aterrissagem.");
        drone.setState(LandingState.getInstance());
    }

    @Override
    public void fly(Drone drone) {
        drone.notifyObservers("Drone está voando normalmente.");
    }

    @Override
    public void emergency(Drone drone) {
        drone.notifyObservers("Emergência em voo! Executando procedimentos de emergência.");
        drone.setState(EmergencyState.getInstance());
    }
}